package dev.floffah.gamermode.events;

import dev.floffah.gamermode.server.Server;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventEmitter {

    Server server;

    Map<Class<? extends Event>, List<Executor>> lnrs = new HashMap<>();

    public EventEmitter(Server server) {
        this.server = server;
    }

    /**
     * Execute a specific event and call all listeners listening to it
     *
     * @param e The event to execute
     */
    public void execute(Event e) {
        if (lnrs.containsKey(e.getClass())) {
            List<Executor> ex = lnrs.get(e.getClass());
            for (Executor exe : ex) {
                exe.execute(e);
            }
        }
    }

    /**
     * Register all EventListeners in a class
     *
     * @param l The class to register from
     */
    public void registerListeners(Listener l) {
        Method[] methods = l.getClass().getMethods();
        for (Method m : methods) {
            EventListener evn = m.getAnnotation(EventListener.class);
            if (evn == null) continue;
            Class<? extends Event> eused;
            if (
                m.getParameterTypes().length != 1 ||
                !Event.class.isAssignableFrom(
                        eused =
                            (Class<? extends Event>) m.getParameterTypes()[0]
                    )
            ) {
                server
                    .getLogger()
                    .error(
                        String.format(
                            "Method %s() in class %s has incorrect parameters",
                            m.getName(),
                            l.getClass().getName()
                        )
                    );
                continue;
            }
            Executor e = new Executor() {
                @Override
                public void execute(Event event) {
                    try {
                        m.invoke(l, event);
                    } catch (
                        IllegalAccessException | InvocationTargetException e
                    ) {
                        server.getLogger().error("Error occurred while invoking an event listener's executor", e);
                    }
                }
            };
            List<Executor> lnr = lnrs.get(eused);
            if (lnr == null) {
                lnr = new ArrayList<>();
            }
            lnr.add(e);
            lnrs.put(eused, lnr);
        }
    }
}
