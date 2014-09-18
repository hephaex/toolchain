/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.integtests;

import org.gradle.integtests.fixtures.executer.IntegrationTestBuildContext;
import org.gradle.internal.UncheckedException;
import org.gradle.openapi.external.ui.SinglePaneUIInteractionVersion1;
import org.gradle.openapi.external.ui.SinglePaneUIVersion1;
import org.gradle.openapi.wrappers.ui.SinglePaneUIWrapper;
import org.gradle.test.fixtures.file.TestDirectoryProvider;
import org.junit.Assert;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OpenApiFixture implements MethodRule {
    private IntegrationTestBuildContext buildContext = new IntegrationTestBuildContext();
    private final TestDirectoryProvider testDirectoryProvider;
    private final List<JFrame> frames = new ArrayList<JFrame>();

    public OpenApiFixture(TestDirectoryProvider testDirectoryProvider) {
        this.testDirectoryProvider = testDirectoryProvider;
    }

    public Statement apply(final Statement base, FrameworkMethod method, final Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    base.evaluate();
                } finally {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            for (JFrame frame : frames) {
                                frame.dispose();
                            }
                        }
                    });
                }
            }
        };
    }

    public SinglePaneUIVersion1 createSinglePaneUI() {
        TestSingleDualPaneUIInteractionVersion1 testSingleDualPaneUIInteractionVersion1 = new TestSingleDualPaneUIInteractionVersion1(new TestAlternateUIInteractionVersion1(), new TestSettingsNodeVersion1());
        return createSinglePaneUI(testSingleDualPaneUIInteractionVersion1);
    }

    public SinglePaneUIVersion1 createSinglePaneUI(SinglePaneUIInteractionVersion1 testSingleDualPaneUIInteractionVersion1) {
        System.setProperty("gradle.home", buildContext.getGradleHomeDir().getAbsolutePath());
        SinglePaneUIVersion1 singlePane;
        try {
            singlePane = new SinglePaneUIWrapper(testSingleDualPaneUIInteractionVersion1, false);
        } catch (Exception e) {
            throw UncheckedException.throwAsUncheckedException(e);
        }

        //make sure we got something
        Assert.assertNotNull(singlePane);

        singlePane.setCurrentDirectory(testDirectoryProvider.getTestDirectory());
        singlePane.addCommandLineArgumentAlteringListener(new ExtraTestCommandLineOptionsListenerWrapper(buildContext.getGradleUserHomeDir()));

        return singlePane;
    }

    public JFrame open(SinglePaneUIVersion1 ui) {
        return createTestFrame(ui.getComponent(), null);
    }

    /*
    * This shows the specified frame for a moment so the event queue can be emptied. This
    * ensures Swing events a processed
    * Don't place anything between the following three lines (especially something that might
    * throw an exception). This shows and hides the UI, giving it time to actually show itself
    * and empty the event dispatch queue.
    */
    public void flushEventQueue(final JFrame frame) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    frame.setVisible(true);
                }
            });
            Thread.sleep(500);
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    frame.setVisible(false);
                }
            });
        } catch (Exception e) {
            throw UncheckedException.throwAsUncheckedException(e);
        }
    }

    private JFrame createTestFrame(Component mainComponent, Component outputComponent) {
        JFrame frame = new JFrame();
        frames.add(frame);
        frame.setSize(650, 500);
        JPanel panel = new JPanel(new BorderLayout());
        frame.getContentPane().add(panel);

        //add a large red label explaining this
        JLabel label2 = new JLabel("Performing Open API Integration Test!");
        label2.setForeground(Color.red);
        label2.setFont(label2.getFont().deriveFont(26f));
        panel.add(label2, BorderLayout.NORTH);

        //add the main UI to the frame
        panel.add(mainComponent, BorderLayout.CENTER);

        if (outputComponent != null) {
            panel.add(outputComponent, BorderLayout.SOUTH);
        }

        return frame;
    }

}
