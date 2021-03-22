package com.viewlift.tv.views.customviews;

import android.content.Context;
import android.view.View;

import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.ModuleWithComponents;
import com.viewlift.models.data.appcms.weather.TickerFeed;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.OnInternalEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import rx.functions.Action1;

/**
 * Created by anas.azeem on 1/2/2018.
 * Owned by ViewLift, NYC
 */

public class TickerView extends TVModuleView {
    private static final String TAG = TickerView.class.getSimpleName();
    private final AppCMSPageAPI appCMSPageAPI;

    public TickerView(Context context,
                      ModuleWithComponents module,
                      Module moduleAPI,
                      AppCMSPageAPI appCMSPageAPI,
                      TVViewCreator tvViewCreator,
                      AppCMSPresenter appCMSPresenter,
                      Map<String, AppCMSUIKeyType> jsonValueKeyMap) {
        super(context, module);
        this.appCMSPageAPI = appCMSPageAPI;

      /*  appCMSPresenter.getTickerFeedXML(cities -> {
            initView(context, module,moduleAPI, tvViewCreator, appCMSPresenter, jsonValueKeyMap);
        });*/

      appCMSPresenter.getTickerFeedXML(moduleAPI != null ? moduleAPI.getApiUrl() : null, new Action1<TickerFeed>() {
          @Override
          public void call(TickerFeed tickerFeed) {
              System.out.println("tickerfeed = "+tickerFeed);
              if(moduleAPI != null) {
                  moduleAPI.setTickerFeed(tickerFeed);
                  initView(context, module, moduleAPI, tvViewCreator, appCMSPresenter, jsonValueKeyMap);
                  setViewRefreshFifteenMinTimer(context, module, moduleAPI, tvViewCreator, appCMSPresenter, jsonValueKeyMap);
              }
          }
      });

    }

    public void initView(Context context,
                         ModuleWithComponents module,
                         Module moduleAPI, TVViewCreator tvViewCreator,
                         AppCMSPresenter appCMSPresenter,
                         Map<String, AppCMSUIKeyType> jsonValueKeyMap) {
        TVViewCreator.ComponentViewResult componentViewResult =
                tvViewCreator.getComponentViewResult();
        List<OnInternalEvent> onInternalEvents = new ArrayList<>();
        for (Component component : module.getComponents()) {
            tvViewCreator.createComponentView(context,
                    component,
                    component.getLayout(),
                    moduleAPI,
                    null,
                    component.getSettings(),
                    jsonValueKeyMap,
                    appCMSPresenter,
                    false,
                    module.getView(),
                    false);

            if (componentViewResult.onInternalEvent != null) {
                onInternalEvents.add(componentViewResult.onInternalEvent);
            }

            View componentView = componentViewResult.componentView;
            if (componentView != null) {
                addChildComponentAndView(componentView , component);
                //setComponentHasView(i, true);
                setViewMarginsFromComponent(component,
                        componentView,
                        component.getLayout(),
                        this,
                        jsonValueKeyMap,
                        false,
                        false,
                        component.getView());

                this.getChildrenContainer().addView(componentView);
            }
        }
    }

    Timer timer;
    TimerTask fifteenMinuteTimerTask;
     public void setViewRefreshFifteenMinTimer(Context context,
                         ModuleWithComponents module,
                         Module moduleAPI,
                         TVViewCreator tvViewCreator,
                         AppCMSPresenter appCMSPresenter,
                         Map<String, AppCMSUIKeyType> jsonValueKeyMap) {
         if(timer != null && fifteenMinuteTimerTask != null){
             fifteenMinuteTimerTask.cancel();
             timer.cancel();
             timer = null;
             fifteenMinuteTimerTask = null;
         }
         timer = new Timer();
         fifteenMinuteTimerTask = new TimerTask() {
            @Override
            public void run() {


                appCMSPresenter.getTickerFeedXML(moduleAPI.getApiUrl(), new Action1<TickerFeed>() {
                    @Override
                    public void call(TickerFeed tickerFeed) {
                        moduleAPI.setTickerFeed(tickerFeed);
                        tvViewCreator.refreshComponentView(
                                context,
                                TickerView.this,
                                module,
                                moduleAPI,
                                tvViewCreator,
                                appCMSPresenter,
                                jsonValueKeyMap
                        );

                    }
                });
            }
        };
        // schedule the task to run starting now and then every hour...
        timer.schedule(fifteenMinuteTimerTask, 1000 * 60 * 15, 1000 * 60 * 15);   // 1000*10*60 every 10 minut
    }
}
