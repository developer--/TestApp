package com.awesomethings.demoapp.events.bus;

import com.squareup.otto.Bus;

/**
 * Created by dev-00 on 6/7/17.
 */

public class MyEventBus {
    private static Bus bus = new Bus();
    private MyEventBus(){}
    public static Bus getBus() {
        return bus;
    }

    public static class FieldsValidCheckerEventModel {}
}
