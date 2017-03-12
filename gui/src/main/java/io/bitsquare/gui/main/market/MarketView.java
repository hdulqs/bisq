/*
 * This file is part of bisq.
 *
 * bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package io.bitsquare.gui.main.market;

import io.bitsquare.gui.Navigation;
import io.bitsquare.gui.common.model.Activatable;
import io.bitsquare.gui.common.view.*;
import io.bitsquare.gui.main.MainView;
import io.bitsquare.gui.main.market.offerbook.OfferBookChartView;
import io.bitsquare.gui.main.market.spread.SpreadView;
import io.bitsquare.gui.main.market.trades.TradesChartsView;
import io.bitsquare.locale.Res;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import javax.inject.Inject;

@FxmlView
public class MarketView extends ActivatableViewAndModel<TabPane, Activatable> {
    @FXML
    Tab offerBookTab, tradesTab, spreadTab;
    private final ViewLoader viewLoader;
    private final Navigation navigation;
    private Navigation.Listener navigationListener;
    private ChangeListener<Tab> tabChangeListener;

    @Inject
    public MarketView(CachingViewLoader viewLoader, Navigation navigation) {
        this.viewLoader = viewLoader;
        this.navigation = navigation;
    }

    @Override
    public void initialize() {
        offerBookTab.setText(Res.get("market.tabs.offerBook"));
        spreadTab.setText(Res.get("market.tabs.spread"));
        tradesTab.setText(Res.get("market.tabs.trades"));
        
        navigationListener = viewPath -> {
            if (viewPath.size() == 3 && viewPath.indexOf(MarketView.class) == 1)
                loadView(viewPath.tip());
        };

        tabChangeListener = (ov, oldValue, newValue) -> {
            if (newValue == offerBookTab)
                navigation.navigateTo(MainView.class, MarketView.class, OfferBookChartView.class);
            else if (newValue == tradesTab)
                navigation.navigateTo(MainView.class, MarketView.class, TradesChartsView.class);
            else if (newValue == spreadTab)
                navigation.navigateTo(MainView.class, MarketView.class, SpreadView.class);
        };
    }

    @Override
    protected void activate() {
        root.getSelectionModel().selectedItemProperty().addListener(tabChangeListener);
        navigation.addListener(navigationListener);

        if (root.getSelectionModel().getSelectedItem() == offerBookTab)
            navigation.navigateTo(MainView.class, MarketView.class, OfferBookChartView.class);
        else if (root.getSelectionModel().getSelectedItem() == tradesTab)
            navigation.navigateTo(MainView.class, MarketView.class, TradesChartsView.class);
        else
            navigation.navigateTo(MainView.class, MarketView.class, SpreadView.class);
    }

    @Override
    protected void deactivate() {
        root.getSelectionModel().selectedItemProperty().removeListener(tabChangeListener);
        navigation.removeListener(navigationListener);
    }

    private void loadView(Class<? extends View> viewClass) {
        final Tab tab;
        View view = viewLoader.load(viewClass);

        if (view instanceof OfferBookChartView) tab = offerBookTab;
        else if (view instanceof TradesChartsView) tab = tradesTab;
        else if (view instanceof SpreadView) tab = spreadTab;
        else throw new IllegalArgumentException("Navigation to " + viewClass + " is not supported");

        if (tab.getContent() != null && tab.getContent() instanceof ScrollPane) {
            ((ScrollPane) tab.getContent()).setContent(view.getRoot());
        } else {
            tab.setContent(view.getRoot());
        }
        root.getSelectionModel().select(tab);
    }

}
