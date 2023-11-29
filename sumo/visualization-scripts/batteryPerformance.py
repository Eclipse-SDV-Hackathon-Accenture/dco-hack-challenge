import argparse
import matplotlib.pyplot as plt
from lxml import etree

def parse_xml(xml_file):
    tree = etree.parse(xml_file)
    root = tree.getroot()

    vehicle_data = {}
    for timestep in root.findall('.//timestep'):
        time = float(timestep.get('time'))
        for vehicle in timestep.findall('.//vehicle'):
            vehicle_id = vehicle.get('id')
            energy_consumed = float(vehicle.get('energyConsumed', 0))
            actual_battery_capacity = float(vehicle.get('actualBatteryCapacity', 0))

            if vehicle_id not in vehicle_data:
                vehicle_data[vehicle_id] = {'time': [], 'energy_consumed': [], 'actual_battery_capacity': []}

            vehicle_data[vehicle_id]['time'].append(time)
            vehicle_data[vehicle_id]['energy_consumed'].append(energy_consumed)
            vehicle_data[vehicle_id]['actual_battery_capacity'].append(actual_battery_capacity)

    return vehicle_data

def plot_and_save(vehicle_data, output_file):
    plt.figure(figsize=(10, 6))

    for vehicle_id, data in vehicle_data.items():
        plt.plot(data['time'], data['actual_battery_capacity'], label=f'Vehicle {vehicle_id}')

    plt.xlabel('Time')
    plt.ylabel('Actual Battery Capacity')
    plt.title('Actual Battery Capacity vs Time')
    plt.legend()
    plt.grid(True)

    plt.savefig(output_file)
    print(f"Plot saved to {output_file}")

def main():
    parser = argparse.ArgumentParser(description='Plot vehicle data from XML file.')
    parser.add_argument('xml_file', type=str, help='Path to the XML file')
    parser.add_argument('--output', type=str, default='plot.png', help='Path to save the plot (default: plot.png)')
    args = parser.parse_args()

    xml_file = args.xml_file
    output_file = args.output

    vehicle_data = parse_xml(xml_file)
    plot_and_save(vehicle_data, output_file)

if __name__ == "__main__":
    main()
