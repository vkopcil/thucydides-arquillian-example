package com.mycompany.listeners;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.thucydides.core.model.DataTable;
import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.steps.ExecutedStepDescription;
import net.thucydides.core.steps.StepFailure;
import net.thucydides.core.steps.StepListener;

import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.spi.Manager;
import org.jboss.arquillian.drone.spi.DroneContext;
import org.jboss.arquillian.graphene.spi.configuration.GrapheneConfiguration;
import org.jboss.arquillian.test.impl.EventTestRunnerAdaptor;
import org.jboss.arquillian.test.spi.LifecycleMethodExecutor;
import org.jboss.arquillian.test.spi.TestRunnerAdaptor;
import org.jboss.arquillian.test.spi.TestRunnerAdaptorBuilder;
import org.jboss.arquillian.test.spi.context.ClassContext;

public class ArquillianListener implements StepListener {

    private static final Logger logger = Logger.getLogger(ArquillianListener.class.getName());

    private enum Mode {
        ACTIVATION,
        REBIND
    };

    private static final Mode mode = Mode.ACTIVATION;

    /*
     * Note about the ThreadLocals.
     * 
     * We have an issue here - the testSuiteFinished() method can be invoked
     * from a different thread, in contrast to the testSuiteStarted() method
     * when the Surefire/Failsafe testrunner runs tests in parallel.
     * 
     * Therefore, a new instance of the ArquillianListener is created because
     * the Thucydides StepEventBus is initalized once again for the new thread.
     * Eventually, the testSuiteFinished() method does not have access to the
     * Arquillian TestRunnerAdaptor and the currentClass instance, i.e. the Test
     * class. This also means that the archive is not undeployed by Arquillian.
     */
    static final ThreadLocal<TestRunnerAdaptor> adaptor = new ThreadLocal<TestRunnerAdaptor>();

    private static final ThreadLocal<Class<?>> currentClass = new ThreadLocal<Class<?>>();

    public void testSuiteStarted(Class<?> paramClass) {
        logger.log(Level.INFO, "Arquillian Test Suite started for test class {0}", paramClass);

        try {
            if (paramClass.equals(currentClass.get())) {
                /*
                 * Suite started for the provided test class. Therefore, return
                 * immediately. This looks like a bug in Thucydides core - a
                 * suite should have been initialized once per class.
                 */
                return;
            } else {
                currentClass.set(paramClass);
            }
            if (adaptor.get() == null) {
                TestRunnerAdaptor arquillianAdaptor = TestRunnerAdaptorBuilder.build();
                adaptor.set(arquillianAdaptor);
            }
            TestRunnerAdaptor testRunner = adaptor.get();
            testRunner.beforeSuite();
            testRunner.beforeClass(paramClass, LifecycleMethodExecutor.NO_OP);

            /**
             * ugly hack ;-) re-bind graphene stuff from class to application
             * context
             */
            if (testRunner instanceof EventTestRunnerAdaptor) {
                Field managerField = EventTestRunnerAdaptor.class.getDeclaredField("manager");
                managerField.setAccessible(true);
                Manager manager = (Manager) managerField.get(testRunner);
                if (mode == Mode.ACTIVATION) {
                    ClassContext context = manager.getContext(ClassContext.class);
                    Class<?> active = context.getActiveId();
                    if (active == null) {
                        context.activate(paramClass);
                    }
                } else if (mode == Mode.REBIND) {
                    if (manager.getContext(GrapheneConfiguration.class) == null) {
                        ClassContext context = manager.getContext(ClassContext.class);
                        Class<?> active = context.getActiveId();
                        if (active == null) {
                            context.activate(paramClass);
                        }
                        GrapheneConfiguration grapheneConfiguration = context.getObjectStore().get(GrapheneConfiguration.class);
                        if (grapheneConfiguration != null) {
                            manager.bind(ApplicationScoped.class, GrapheneConfiguration.class, grapheneConfiguration);
                        }
                        DroneContext droneContext = context.getObjectStore().get(DroneContext.class);
                        if (droneContext != null) {
                            manager.bind(ApplicationScoped.class, DroneContext.class, droneContext);
                        }
                        if (active == null) {
                            context.deactivate();
                        }
                    }
                }
            }
            /**
             * end of hack
             */

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void testSuiteFinished() {
        logger.log(Level.INFO, "Arquillian Test Suite finished.");
        try {
            TestRunnerAdaptor testRunner = adaptor.get();
            Class<?> klass = currentClass.get();
            if (testRunner != null && klass != null) {
                testRunner.afterClass(klass, LifecycleMethodExecutor.NO_OP);
                testRunner.afterSuite();
            }
            if (testRunner instanceof EventTestRunnerAdaptor) {
                Field managerField = EventTestRunnerAdaptor.class.getDeclaredField("manager");
                managerField.setAccessible(true);
                Manager manager = (Manager) managerField.get(testRunner);
                if (mode == Mode.ACTIVATION) {
                    ClassContext context = manager.getContext(ClassContext.class);
                    context.deactivate();
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void skippedStepStarted(ExecutedStepDescription paramExecutedStepDescription) {
        // TODO Auto-generated method stub

    }

    public void stepFailed(StepFailure paramStepFailure) {
        // TODO Auto-generated method stub

    }

    public void stepIgnored() {
        // TODO Auto-generated method stub

    }

    public void stepPending() {
        // TODO Auto-generated method stub

    }

    public void stepFinished() {
        // TODO Auto-generated method stub

    }

    public void testFailed(Throwable paramThrowable) {
        // TODO Auto-generated method stub

    }

    public void testIgnored() {
        // TODO Auto-generated method stub

    }

    public void notifyScreenChange() {
        // TODO Auto-generated method stub

    }

    @Override
    public void lastStepFailed(StepFailure arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stepPending(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void testFailed(TestOutcome arg0, Throwable arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void testRetried() {
        // TODO Auto-generated method stub

    }

    @Override
    public void useExamplesFrom(DataTable table) {
        // TODO Auto-generated method stub

    }

    @Override
    public void exampleStarted(Map<String, String> data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void exampleFinished() {
        // TODO Auto-generated method stub

    }

    @Override
    public void assumptionViolated(String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void testSuiteStarted(Story story) {
        // TODO Auto-generated method stub

    }

    @Override
    public void testStarted(String description) {
        // TODO Auto-generated method stub

    }

    @Override
    public void testFinished(TestOutcome result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stepStarted(ExecutedStepDescription description) {
        // TODO Auto-generated method stub

    }

}
