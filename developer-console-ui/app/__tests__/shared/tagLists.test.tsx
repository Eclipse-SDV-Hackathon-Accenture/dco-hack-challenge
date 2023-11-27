import { render } from '@testing-library/react';
import { StoreProvider } from 'easy-peasy';
import { store } from '../../services/store.service';
import TagLists from '../../pages/shared/tagLists';
describe('TagLists Page', () => {

    test('TagLists page render', async () => {
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <TagLists data={{ getReleaseById: { countries: ['IND'] } }} type={'countries'} from={'relaseDetails'} />
            </StoreProvider>
        )
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <TagLists data={{ getReleaseById: { brands: ['Test'] } }} type={'brands'} from={'relaseDetails'} />
            </StoreProvider>
        )
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <TagLists data={{ getReleaseById: { models: ['Test'] } }} type={'models'} from={'relaseDetails'} />
            </StoreProvider>
        )
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <TagLists data={['test']} type={'countries'} from={'trackDetails'} />
            </StoreProvider>
        )
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <TagLists data={['test']} type={'brands'} from={'trackDetails'} />
            </StoreProvider>
        )
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <TagLists data={[{ name: 'test', version: '1.1.1' }]} type={'compNVersion'} from={'trackDetails'} />
            </StoreProvider>
        )
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <TagLists data={['test']} type={'devices'} from={'trackDetails'} />
            </StoreProvider>
        )
    });
}) 
