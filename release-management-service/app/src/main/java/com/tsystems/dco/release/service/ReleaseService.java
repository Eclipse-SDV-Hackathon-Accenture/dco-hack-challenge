package com.tsystems.dco.release.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tsystems.dco.base.BaseException;
import com.tsystems.dco.exception.IdNotFoundException;
import com.tsystems.dco.release.domain.ApproverTrackGate;
import com.tsystems.dco.release.domain.QualityGateWithApprovers;
import com.tsystems.dco.release.domain.QualityGateWithApproversList;
import com.tsystems.dco.release.entity.EcuDataEntity;
import com.tsystems.dco.release.entity.FunctionEntity;
import com.tsystems.dco.release.entity.QualityGateWithApproversEntity;
import com.tsystems.dco.release.entity.QualityGateWithApproversListEntity;
import com.tsystems.dco.release.entity.ReleaseEntity;
import com.tsystems.dco.release.integration.track.client.TrackApiClient;
import com.tsystems.dco.release.integration.track.domain.ComponentStatusData;
import com.tsystems.dco.release.integration.track.domain.TrackCampaignEntity;
import com.tsystems.dco.release.integration.track.domain.VehicleResponse;
import com.tsystems.dco.release.mapper.ReleaseMapper;
import com.tsystems.dco.release.model.*;
import com.tsystems.dco.release.repository.QualityGateWithApproversRepository;
import com.tsystems.dco.release.repository.ReleaseRepository;
import com.tsystems.dco.release.repository.TrackCampaignRepository;
import com.tsystems.dco.tm.integration.tminterface.client.TaskManagerServiceApiClient;
import com.tsystems.dco.tm.integration.tminterface.domain.WorkFlowRequest;
import com.tsystems.dco.tm.integration.workflowregistry.client.TaskManagerWorkflowRegistryServiceApiClient;
import com.tsystems.dco.tm.integration.workflowregistry.domain.Definition;
import com.tsystems.dco.tm.integration.workflowregistry.domain.State;
import com.tsystems.dco.tm.integration.workflowregistry.domain.Action;
import com.tsystems.dco.tm.integration.workflowregistry.domain.Function;
import com.tsystems.dco.tm.integration.workflowregistry.domain.WorkflowDefinitionRequest;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * ReleaseService
 */
@Service
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
@RequiredArgsConstructor
public class ReleaseService {

  private static final Logger logger = LoggerFactory.getLogger(ReleaseService.class);
  private static final String APPROVER1 = "ajit.panda@t-systems.com";
  private static final String APPROVER2 = "tanyel.tuncer@t-systems.com";
  private static final String SECURITY_WF = "Security Update Testing 1";
  private static final String COMPONENT_STATUS = "READY";
  private final ReleaseRepository releaseRepository;
  private final QualityGateWithApproversRepository qualityGateWithApproversRepository;
  private final TrackCampaignRepository trackCampaignRepository;
  private final TrackApiClient trackApiClient;
  private final TaskManagerWorkflowRegistryServiceApiClient workflowRegistryApiClient;
  private final TaskManagerServiceApiClient taskManagerServiceApiClient;

  /**
   * @param input
   * @return String
   */
  @Transactional
  public Release createRelease(CreateReleaseRequest input) {
    if (releaseRepository.existsById(input.getReleaseId())) {
      logger.error("This ID is already used for another release, please define another ID");
      throw new BaseException(HttpStatus.BAD_REQUEST, "This ID is already used for another release, please define another ID");
    }
    QualityGateWithApproversList gateWithApproversList = setQualityGateAndApprovers(input.getMetaTrack());
    gateWithApproversList.setReleaseId(input.getReleaseId());
    insertQualityGateWithApprovers(gateWithApproversList);

    input.setReleaseStatus(ReleaseStatus.TESTING);
    ReleaseEntity entity = ReleaseMapper.INSTANCE.toEntity(input);
    entity.setCreatedDate(LocalDate.now().toString());
    entity = releaseRepository.save(entity);

    logger.info("QG-1 is running");
    Optional<QualityGateWithApproversListEntity> optionalGateEntity = qualityGateWithApproversRepository.findById(entity.getReleaseId());
    if (optionalGateEntity.isPresent()) {
      List<QualityGateWithApproversEntity> gateWithApproversEntities = optionalGateEntity.get().getGatesWithApprovers();
      Optional<QualityGateWithApproversEntity> gateWithApproversEntity = gateWithApproversEntities.stream().filter(x -> (x.getQualityGate() == 1)).findAny();
        gateWithApproversEntity.ifPresent(qualityGateWithApproversEntity -> qualityGateWithApproversEntity.setStatus(GateStatus.INPROGRESS));
      logger.info("Quality Gate 1 started");
      Set<FunctionEntity> set = entity.getFunctions();
      Optional<FunctionEntity> functionEntity = set.stream().findFirst();
      if (Boolean.TRUE.equals(gateWithApproversEntity.isPresent()) && Boolean.TRUE.equals(functionEntity.isPresent())) {
        Set<EcuDataEntity> ecuDatas = functionEntity.get().getEcuDatas();
        Optional<EcuDataEntity> ecuDataEntity = ecuDatas.stream().findFirst();
        if (ecuDataEntity.isPresent()) {
          final var trackIdList = fetchTrackIds(gateWithApproversEntity.get().getTracks());
          var campaignId = storeCampaign(trackIdList, ecuDataEntity.get().getComponentVersion());
          logger.info("Q1 Campaign - {}", campaignId);
        }
      }
    }
    return ReleaseMapper.INSTANCE.toModel(entity);
  }

  /**
   * @return QualityGateWithApproversList
   */

  private QualityGateWithApproversList setQualityGateAndApprovers(String metaTrack) {
    if (metaTrack.equals("workflow-release-testing-germany-1")) {
      ApproverTrackGate germanyGate1 = ApproverTrackGate.builder().qualityGate(1).approver(APPROVER1).tracks(List.of("Alpha Testing Germany 1")).build();
      ApproverTrackGate germanyGate2 = ApproverTrackGate.builder().qualityGate(2).approver(APPROVER1).tracks(List.of("Alpha Testing Germany 2")).build();
      ApproverTrackGate germanyGate3 = ApproverTrackGate.builder().qualityGate(3).approver(APPROVER1).tracks(List.of("Alpha Testing Germany 3")).build();
      List<ApproverTrackGate> approverTrackGates = Arrays.asList(germanyGate1, germanyGate2, germanyGate3);
      return setApproversAndTracks(approverTrackGates);
    } else if (metaTrack.equals("workflow-exhaust-americas-1")) {
      ApproverTrackGate americaGate1 = ApproverTrackGate.builder().qualityGate(1).approver(APPROVER1).tracks(List.of("Alpha Testing Americas 1")).build();
      ApproverTrackGate americaGate2 = ApproverTrackGate.builder().qualityGate(2).approver(APPROVER2).tracks(List.of("Beta Testing Americas 1")).build();
      ApproverTrackGate americaGate3 = ApproverTrackGate.builder().qualityGate(3).approver(APPROVER2).tracks(List.of("Beta Testing Americas 1")).build();
      List<ApproverTrackGate> approverTrackGates = Arrays.asList(americaGate1, americaGate2, americaGate3);
      return setApproversAndTracks(approverTrackGates);
    } else if (metaTrack.equals("workflow-homologation-europe-1")) {
      ApproverTrackGate europeGate1 = ApproverTrackGate.builder().qualityGate(1).approver(APPROVER1).tracks(List.of("Alpha Testing Europe 1")).build();
      ApproverTrackGate europeGate2 = ApproverTrackGate.builder().qualityGate(2).approver(APPROVER1).tracks(List.of("Beta Testing Europe 1")).build();
      ApproverTrackGate europeGate3 = ApproverTrackGate.builder().qualityGate(3).approver(APPROVER1).tracks(List.of("Beta Testing Europe 2")).build();
      List<ApproverTrackGate> approverTrackGates = Arrays.asList(europeGate1, europeGate2, europeGate3);
      return setApproversAndTracks(approverTrackGates);
    } else if (metaTrack.equals("workflow-autonomous-lvl5-driving-china-1")) {
      ApproverTrackGate chinaGate1 = ApproverTrackGate.builder().qualityGate(1).approver(APPROVER1).tracks(List.of("Autonomous Vehicle lvl 5 Testing")).build();
      ApproverTrackGate chinaGate2 = ApproverTrackGate.builder().qualityGate(2).approver(APPROVER2).tracks(List.of("Alpha Testing China 1")).build();
      ApproverTrackGate chinaGate3 = ApproverTrackGate.builder().qualityGate(3).approver(APPROVER2).tracks(List.of("Beta Testing China 1")).build();
      List<ApproverTrackGate> approverTrackGates = Arrays.asList(chinaGate1, chinaGate2, chinaGate3);
      return setApproversAndTracks(approverTrackGates);
    } else {
      ApproverTrackGate regionGate1 = ApproverTrackGate.builder().qualityGate(1).approver(APPROVER1).tracks(List.of(SECURITY_WF)).build();
      ApproverTrackGate regionGate2 = ApproverTrackGate.builder().qualityGate(2).approver(APPROVER2).tracks(List.of(SECURITY_WF)).build();
      ApproverTrackGate regionGate3 = ApproverTrackGate.builder().qualityGate(3).approver(APPROVER2).tracks(List.of(SECURITY_WF)).build();
      List<ApproverTrackGate> approverTrackGates = Arrays.asList(regionGate1, regionGate2, regionGate3);
      return setApproversAndTracks(approverTrackGates);
    }
  }

  private QualityGateWithApproversList setApproversAndTracks(List<ApproverTrackGate> approverTrackGates) {
    List<QualityGateWithApprovers> gateWithApproversList = new ArrayList<>();
    approverTrackGates.forEach(x -> {
      if (x.getQualityGate() == 1) {
        var gateWithApprovers1 = new QualityGateWithApprovers();
        gateWithApprovers1.setQualityGate(1);
        gateWithApprovers1.setIsPassed(false);
        gateWithApprovers1.setApprovers(Collections.singletonList(x.getApprover()));
        gateWithApprovers1.setTracks(x.getTracks());
        gateWithApprovers1.setStatus(GateStatus.PENDING.name());
        gateWithApproversList.add(gateWithApprovers1);
      } else if (x.getQualityGate() == 2) {
        var gateWithApprovers2 = new QualityGateWithApprovers();
        gateWithApprovers2.setQualityGate(2);
        gateWithApprovers2.setIsPassed(false);
        gateWithApprovers2.setApprovers(Collections.singletonList(x.getApprover()));
        gateWithApprovers2.setTracks(x.getTracks());
        gateWithApprovers2.setStatus(GateStatus.PENDING.name());
        gateWithApproversList.add(gateWithApprovers2);
      } else if (x.getQualityGate() == 3) {
        var gateWithApprovers3 = new QualityGateWithApprovers();
        gateWithApprovers3.setQualityGate(3);
        gateWithApprovers3.setIsPassed(false);
        gateWithApprovers3.setApprovers(Collections.singletonList(x.getApprover()));
        gateWithApprovers3.setTracks(x.getTracks());
        gateWithApprovers3.setStatus(GateStatus.PENDING.name());
        gateWithApproversList.add(gateWithApprovers3);
      }
    });
    var list = new QualityGateWithApproversList();
    list.setGatesWithApprovers(gateWithApproversList);
    return list;
  }

  private List<UUID> fetchTrackIds(List<String> patterns) {
    List<UUID> uuids = new ArrayList<>();

    Pattern regExPattern;
    try {
      regExPattern = Pattern.compile("\\bid=([0-9a-fA-F-]+),");
    } catch (PatternSyntaxException e) {
      logger.error("Invalid regex pattern: {}", e.getDescription());
      return Collections.emptyList();
    }

    for (String pattern : patterns)
    {
      final var trackResponseEntity = trackApiClient.searchTrackByMatchingPattern(pattern, 0, 100);
      if (trackResponseEntity == null) {
        logger.warn("Tracks of pattern: {} not found", pattern);
        continue;
      }
      final var bodyString = Objects.requireNonNull(trackResponseEntity.getBody()).toString();

      Matcher matcher = regExPattern.matcher(bodyString);
      while (matcher.find()) {
        final var id = UUID.fromString(matcher.group(1));
        if (!uuids.contains(id)) {
          uuids.add(id);
        }
      }
    }
    return uuids;
  }

  private UUID storeCampaign(List<UUID> uuids, String componentVersion) {
    UUID campaignId = null;
    for (UUID id : uuids) {
      var trackResponseEntity = trackApiClient.findTrackById(id);
      var trackResponse = trackResponseEntity.getBody();
      var trackCampaignEntity = new TrackCampaignEntity();
      if (trackResponse != null) {
        trackCampaignEntity.setVin(trackResponse.getVehicles().stream().map(VehicleResponse::getVin).collect(Collectors.toList()));
        trackCampaignEntity.setName(trackResponse.getName());
        trackCampaignEntity.setDescription(trackResponse.getDescription());
        trackCampaignEntity.setEndDate(trackResponse.getDuration());
        trackCampaignEntity.setVersion(componentVersion);
        logger.info("campaign request - {}", trackCampaignEntity);
        trackCampaignRepository.save(trackCampaignEntity);
        campaignId = trackCampaignEntity.getId();
        logger.info("campaign Id {}", campaignId);
      }
    }
    return campaignId;
  }

  /**
   * @param input
   * @return String
   */
  @Transactional
  public String insertQualityGateWithApprovers(QualityGateWithApproversList input) {
    QualityGateWithApproversListEntity entity = ReleaseMapper.INSTANCE.toEntity(input);
    entity = qualityGateWithApproversRepository.save(entity);
    return String.format("saved quality gate %s", entity.getReleaseId());
  }

  /**
   * @return BrandModelCountryResponse
   */
  public BrandModelCountry getBrandModelCountryList() {
    List<String> mercedesModels = Arrays.asList("A class", "B class", "C class", "E class");
    List<String> hyperModels = Arrays.asList("H1", "H2", "H3", "H5");
    List<String> audiModels = Arrays.asList("A1", "A2", "A3", "A4", "A5");
    List<String> bmwModels = Arrays.asList("1 series", "2 series", "3 series", "4 series", "5 series");

    BrandModel mercedes = BrandModel.builder().name("Mercedes").models(mercedesModels).build();
    BrandModel hyper = BrandModel.builder().name("Hyper.Car").models(hyperModels).build();
    BrandModel audi = BrandModel.builder().name("Audi").models(audiModels).build();
    BrandModel bmw = BrandModel.builder().name("BMW").models(bmwModels).build();

    List<BrandModel> brands = Arrays.asList(hyper, mercedes, audi, bmw);

    List<String> countries = Arrays.asList("Austria", "Belgium", "Brazil", "China", "Denmark", "France", "Switzerland", "United Kingdom", "United States", "Germany", "Italy");

    return BrandModelCountry.builder().brands(brands).countries(countries).build();
  }

  /**
   * @return List<String>
   */
  public List<String> getMetaTrack() {
    return Arrays.asList("workflow-release-testing-germany-1", "workflow-exhaust-americas-1", "workflow-homologation-europe-1", "workflow-autonomous-lvl5-driving-china-1", "workflow-security-update-all-regions-1");
  }

  /**
   * @return List<ListReleaseResponse>
   */
  @Transactional
  public List<Release> listTrackRelease() {
    List<ReleaseEntity> releaseEntities = releaseRepository.findAll();
    List<Release> listReleaseResponses = new ArrayList<>();
    releaseEntities.forEach(x -> {
      var listReleaseResponse = ReleaseMapper.INSTANCE.toModel(x);
      listReleaseResponses.add(listReleaseResponse);
    });
    return listReleaseResponses;
  }

  /**
   * @param releaseId
   * @return ListReleaseResponse
   */
  @Transactional
  public Release getReleaseById(String releaseId) {
    Optional<ReleaseEntity> optionalRelease = releaseRepository.findById(releaseId);
    if (optionalRelease.isPresent()) return ReleaseMapper.INSTANCE.toModel(optionalRelease.get());
    else throw new IdNotFoundException(HttpStatus.NOT_FOUND, "Release ID not Found");
  }

  public List<FunctionDataSet> getFunctionDataSet() {
    List<String> bcs = Arrays.asList("Thermal Management", "Fast/slow Charging", "Sleep Mode Controller");
    List<String> bcm = List.of("Firmware");
    List<String> tcu = Arrays.asList("Modem", "Amplifier", "GNSS", "eSIM");
    List<String> ivi = List.of("Regional Settings");
    List<String> hud = List.of("Beamer");

    EcuDataSet bcs101 = EcuDataSet.builder().ecu("BCS-10-B1X").hardwares(bcs).build();
    EcuDataSet bcs102 = EcuDataSet.builder().ecu("BCS-10-B2X").hardwares(bcs).build();
    EcuDataSet bcs0 = EcuDataSet.builder().ecu("BCS-00-B2X").hardwares(bcs).build();
    EcuDataSet bcm0 = EcuDataSet.builder().ecu("BCS-10-B1X").hardwares(bcm).build();
    EcuDataSet tcu0 = EcuDataSet.builder().ecu("TCU-00-V2X").hardwares(tcu).build();
    EcuDataSet ivi0 = EcuDataSet.builder().ecu("IVI-00-S1A").hardwares(ivi).build();
    EcuDataSet hud0 = EcuDataSet.builder().ecu("HUD-00-H2X").hardwares(hud).build();

    List<EcuDataSet> chargingSets = Arrays.asList(bcs101, bcs102);
    List<EcuDataSet> plugChargeSets = Collections.singletonList(bcs0);
    List<EcuDataSet> parkingSets = Arrays.asList(bcm0, tcu0);
    List<EcuDataSet> inCarSets = Collections.singletonList(ivi0);
    List<EcuDataSet> acSets = Collections.singletonList(hud0);

    FunctionDataSet chargingFunction = FunctionDataSet.builder().name("Charging").ecus(chargingSets).build();
    FunctionDataSet plugChargeFunction = FunctionDataSet.builder().name("Plug and Charge").ecus(plugChargeSets).build();
    FunctionDataSet parkingFunction = FunctionDataSet.builder().name("Automated Parking").ecus(parkingSets).build();
    FunctionDataSet inCarFunction = FunctionDataSet.builder().name("In Car Shop").ecus(inCarSets).build();
    FunctionDataSet acFunction = FunctionDataSet.builder().name("Air Conditioner ").ecus(acSets).build();

    return Arrays.asList(chargingFunction, plugChargeFunction, parkingFunction, inCarFunction, acFunction);
  }

  /**
   * @param approverStatus
   * @return String
   */
  @Transactional
  public String updateQualityGateWithApprovers(UpdateApproverStatus approverStatus) {
    Optional<QualityGateWithApproversListEntity> optionalGateEntity = qualityGateWithApproversRepository.findById(approverStatus.getReleaseId());
    if (optionalGateEntity.isPresent()) {
      List<QualityGateWithApproversEntity> gateWithApproversEntities = optionalGateEntity.get().getGatesWithApprovers();
      Optional<QualityGateWithApproversEntity> gateOne = gateWithApproversEntities.stream().filter(y -> (y.getQualityGate() == 1)).findAny();
      Optional<QualityGateWithApproversEntity> gateTwo = gateWithApproversEntities.stream().filter(y -> (y.getQualityGate() == 2)).findAny();
      Optional<QualityGateWithApproversEntity> gateThree = gateWithApproversEntities.stream().filter(y -> (y.getQualityGate() == 3)).findAny();
      var listReleaseResponse = getReleaseById(approverStatus.getReleaseId());
      List<FunctionData> functionDataResponses = listReleaseResponse.getFunctions();
      Optional<FunctionData> functionDataResponse = functionDataResponses.stream().findFirst();
      if (gateOne.isPresent() && gateTwo.isPresent() && functionDataResponse.isPresent() && approverStatus.getQualityGate().equals(gateOne.get().getQualityGate())) {
        QualityGateWithApproversEntity qg1 = gateOne.get();
        qg1.setIsPassed(true);
        qg1.setStatus(approverStatus.getStatus());
        qg1.setApprovedDate(LocalDate.now().toString());
        //QG2
        QualityGateWithApproversEntity qg2 = gateTwo.get();
        qg2.setStatus(GateStatus.INPROGRESS);
        logger.info("Quality Gate 1 passed and Quality Gate 2 started");
        var campaignId = storeCampaign(gateTwo.get().getTrackIds(), functionDataResponse.get().getEcuDatas().stream().findFirst().get().getComponentVersion());
        logger.info("Q1 Campaign - {}", campaignId);
      } else if (Boolean.TRUE.equals(gateTwo.isPresent()) && Boolean.TRUE.equals(gateThree.isPresent()) && approverStatus.getQualityGate().equals(gateTwo.get().getQualityGate()) && Boolean.TRUE.equals(gateOne.isPresent()) && Boolean.TRUE.equals(gateOne.get().getIsPassed())) {
        QualityGateWithApproversEntity qg2 = gateTwo.get();
        qg2.setIsPassed(true);
        qg2.setStatus(GateStatus.PASSED);
        qg2.setApprovedDate(LocalDate.now().toString());
        //QG3
        QualityGateWithApproversEntity qg3 = gateThree.get();
        qg3.setStatus(GateStatus.INPROGRESS);
        logger.info("Quality Gate 2 passed and Quality Gate 3 started");
        var campaignId = storeCampaign(gateThree.get().getTrackIds(), functionDataResponse.get().getEcuDatas().stream().findFirst().get().getComponentVersion());
        logger.info("Q2 Campaign - {}", campaignId);
      } else if (Boolean.TRUE.equals(gateThree.isPresent()) && approverStatus.getQualityGate().equals(gateThree.get().getQualityGate()) && Boolean.TRUE.equals(gateTwo.isPresent()) && Boolean.TRUE.equals(gateTwo.get().getIsPassed()) && Boolean.TRUE.equals(gateOne.isPresent()) && Boolean.TRUE.equals(gateOne.get().getIsPassed())) {
        QualityGateWithApproversEntity qg3 = gateThree.get();
        qg3.setIsPassed(true);
        qg3.setStatus(GateStatus.PASSED);
        qg3.setApprovedDate(LocalDate.now().toString());
        listReleaseResponse.setReleaseStatus(ReleaseStatus.READY_FOR_RELEASE);
        ReleaseEntity releaseEntity = ReleaseMapper.INSTANCE.toEntity(listReleaseResponse);
        releaseRepository.save(releaseEntity);

        //update component status
        functionDataResponses.forEach(z ->
          z.getEcuDatas().stream().forEach(x -> {
              ComponentStatusData status = ComponentStatusData.builder().status(COMPONENT_STATUS).componentId(x.getComponentId()).build();
              logger.info("component status data {}", status);
              trackApiClient.updateSoftwareComponentStatus(status);
            }
          )
        );
        releaseEntity.getFunctions().stream().forEach(f -> {
          f.getEcuDatas().stream().forEach(k -> {
            k.setStatus(COMPONENT_STATUS);
            releaseRepository.save(releaseEntity);
          });
        });
      }
      // To maintain order of gates
      List<QualityGateWithApproversEntity> orderedGates = new ArrayList<>();
      gateWithApproversEntities.forEach(orderedGates::add);
      optionalGateEntity.get().setGatesWithApprovers(orderedGates);
    }
    return "Approved";
  }

  /**
   * @param releaseId
   * @return QualityGateWithApproversListResponse
   */
  @Transactional
  public ReleaseApprovers getQualitygateAndApproversStatus(String releaseId) {
    Optional<QualityGateWithApproversListEntity> optionalGate = qualityGateWithApproversRepository.findById(releaseId);
    if (optionalGate.isPresent()) {
      return ReleaseMapper.INSTANCE.toModel(optionalGate.get());
    } else {
      throw new IdNotFoundException(HttpStatus.NOT_FOUND, "Release ID not Found");
    }
  }

  /**
   * @param workflowDefinition
   * @return
   */
  public String createWorkflowDefinition(WorkflowDefinition workflowDefinition) {
    WorkflowDefinitionRequest workflowDefinitionRequest = new WorkflowDefinitionRequest();
    Definition definition = new Definition();
    List<State> state = new ArrayList();
    List<Action> action = new ArrayList<>();
    List<Function> functions = new ArrayList<>();

    /*Hard code data start*/
    Action actions = new Action("QualityGates QA-action", "QualityGates QA-function");
    action.add(actions);

    Function func = new Function("custom", "QualityGates QA-function", "release-management-service.qualityGates");
    functions.add(func);

    State states = new State("QualityGates QA", "operation", action, true);
    state.add(states);

    definition.setId(workflowDefinition.getType());
    definition.setName(workflowDefinition.getType());
    definition.setVersion("1.0");
    definition.setSpecVersion("0.8");
    definition.setStart("QualityGates QA");

    /*Hard code data end */
    definition.setStates(state);
    definition.setFunctions(functions);
    workflowDefinitionRequest.setDefinition(definition);
    workflowDefinitionRequest.setNamespace(workflowDefinition.getNamespace());
    workflowDefinitionRequest.setType(workflowDefinition.getType());
    workflowDefinitionRequest.setDescription(workflowDefinition.getDescription());
    workflowRegistryApiClient.createWorkflowRegistry(workflowDefinitionRequest);
    return workflowDefinition.getNamespace() + "." + workflowDefinition.getType();

  }

  /**
   * @param id
   * @return definition by id
   */
  public DefinitionResponse getWorkflowDefinitionById(String id) {
    var workflowDefinitionRequest = workflowRegistryApiClient.getWorkflowDefinitionById(id).getBody();
      return ReleaseMapper.INSTANCE.toEntity(workflowDefinitionRequest);
  }

  /**
   * @return list of all definitions
   */
  public List<DefinitionResponse> getAllWorkflowDefinition() {
      var workflowDefinitionRequest = workflowRegistryApiClient.getAllWorkflowDefinition().getBody();
      if(workflowDefinitionRequest ==null ){
        throw new IdNotFoundException(HttpStatus.NOT_FOUND, "No Workflow definition found");
      }
      var allWorkflowDefinitionRequest = workflowDefinitionRequest.
        stream().filter(x -> x.getNamespace().equals("DCO")).toList();
      return ReleaseMapper.INSTANCE.toEntity(allWorkflowDefinitionRequest);
  }

  /**
   * @param qualityGateRequest
   * @return
   */
  @Transactional
  public String createQualityGateWithApprovers(CreateQualityGateWithApprovers qualityGateRequest) {
    WorkFlowRequest workFlowRequest = new WorkFlowRequest();
    workFlowRequest.setNamespace("DCO");
    workFlowRequest.setTaskQueue("release-management-service");
    workFlowRequest.setWorkflowType("quality-gate-execution");
    workFlowRequest.setPayload(qualityGateRequest);
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    ResponseEntity<String> responseEntity = taskManagerServiceApiClient.createWorkflow(workFlowRequest);
    return responseEntity.getBody();
  }

  @Transactional
  public Release deleteReleaseById(String releaseId) {
    Optional<ReleaseEntity> optionalRelease = releaseRepository.findById(releaseId);
    if (optionalRelease.isPresent()) {
      releaseRepository.deleteById(releaseId);
      return ReleaseMapper.INSTANCE.toModel(optionalRelease.get());
    }
    else throw new IdNotFoundException(HttpStatus.NOT_FOUND, "Release ID not Found");
  }

}
