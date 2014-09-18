/*
 * Copyright 2010 the original author or authors.
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

package org.gradle.api.tasks;

import groovy.lang.Closure;
import org.gradle.api.Action;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.internal.AbstractTask;
import org.gradle.internal.Actions;
import org.gradle.api.internal.DependencyInjectingInstantiator;
import org.gradle.api.internal.project.AbstractProject;
import org.gradle.api.internal.project.DefaultProject;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.internal.project.taskfactory.ITaskFactory;
import org.gradle.api.internal.tasks.TaskExecuter;
import org.gradle.api.internal.tasks.TaskExecutionContext;
import org.gradle.api.internal.tasks.TaskStateInternal;
import org.gradle.api.specs.Spec;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.internal.service.DefaultServiceRegistry;
import org.gradle.test.fixtures.file.TestNameTestDirectoryProvider;
import org.gradle.util.GUtil;
import org.gradle.util.TestUtil;
import org.gradle.util.JUnit4GroovyMockery;
import org.jmock.Expectations;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.*;

public abstract class AbstractTaskTest {
    public static final String TEST_TASK_NAME = "testTask";
    @Rule
    public TestNameTestDirectoryProvider tmpDir = new TestNameTestDirectoryProvider();

    protected JUnit4GroovyMockery context = new JUnit4GroovyMockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    protected DefaultServiceRegistry serviceRegistry = new DefaultServiceRegistry();

    protected Instantiator instantiator = new DependencyInjectingInstantiator(serviceRegistry);

    private AbstractProject project = TestUtil.createRootProject();

    public abstract AbstractTask getTask();

    public <T extends AbstractTask> T createTask(Class<T> type) {
        return createTask(type, project, TEST_TASK_NAME);
    }

    public Task createTask(ProjectInternal project, String name) {
        return createTask(getTask().getClass(), project, name);
    }

    public <T extends AbstractTask> T createTask(Class<T> type, ProjectInternal project, String name) {
        Task task = project.getServices().get(ITaskFactory.class).createTask(GUtil.map(Task.TASK_TYPE, type, Task.TASK_NAME, name));
        assertTrue(type.isAssignableFrom(task.getClass()));
        return type.cast(task);
    }

    @Before
    public final void setupRegistry() {
        serviceRegistry.add(Instantiator.class, instantiator);
    }

    @Test
    public void testTask() {
        assertTrue(getTask().isEnabled());
        assertEquals(TEST_TASK_NAME, getTask().getName());
        assertNull(getTask().getDescription());
        assertSame(project, getTask().getProject());
        assertNotNull(getTask().getStandardOutputCapture());
        assertNotNull(getTask().getInputs());
        assertNotNull(getTask().getOutputs());
        assertNotNull(getTask().getOnlyIf());
        assertTrue(getTask().getOnlyIf().isSatisfiedBy(getTask()));
    }

    @Test
    public void testPath() {
        DefaultProject rootProject = TestUtil.createRootProject();
        DefaultProject childProject = TestUtil.createChildProject(rootProject, "child");
        childProject.getProjectDir().mkdirs();
        DefaultProject childchildProject = TestUtil.createChildProject(childProject, "childchild");
        childchildProject.getProjectDir().mkdirs();

        Task task = createTask(rootProject, TEST_TASK_NAME);
        assertEquals(Project.PATH_SEPARATOR + TEST_TASK_NAME, task.getPath());
        task = createTask(childProject, TEST_TASK_NAME);
        assertEquals(Project.PATH_SEPARATOR + "child" + Project.PATH_SEPARATOR + TEST_TASK_NAME, task.getPath());
        task = createTask(childchildProject, TEST_TASK_NAME);
        assertEquals(Project.PATH_SEPARATOR + "child" + Project.PATH_SEPARATOR + "childchild" + Project.PATH_SEPARATOR + TEST_TASK_NAME, task.getPath());
    }

    @Test
    public void testToString() {
        assertEquals("task '" + getTask().getPath() + "'", getTask().toString());
    }

    @Test
    public void testDeleteAllActions() {
        Action<? super Task> action1 = Actions.<Task>doNothing();
        Action<? super Task> action2 = Actions.<Task>doNothing();
        getTask().doLast(action1);
        getTask().doLast(action2);
        assertSame(getTask(), getTask().deleteAllActions());
        assertTrue(getTask().getActions().isEmpty());
    }

    @Test(expected = InvalidUserDataException.class)
    public void testAddActionWithNull() {
        getTask().doLast((Closure) null);
    }

    @Test
    public void testExecuteDelegatesToTaskExecuter() {
        final AbstractTask task = getTask();

        final TaskExecuter executer = context.mock(TaskExecuter.class);
        task.setExecuter(executer);

        context.checking(new Expectations() {{
            one(executer).execute(with(sameInstance(task)), with(notNullValue(TaskStateInternal.class)), with(notNullValue(TaskExecutionContext.class)));
        }});

        task.execute();
    }

    public AbstractProject getProject() {
        return project;
    }

    public void setProject(AbstractProject project) {
        this.project = project;
    }

    @Test
    public void setGetDescription() {
        String testDescription = "testDescription";
        getTask().setDescription(testDescription);
        assertEquals(testDescription, getTask().getDescription());
    }

    @Test
    public void canSpecifyOnlyIfPredicateUsingClosure() {
        AbstractTask task = getTask();
        assertTrue(task.getOnlyIf().isSatisfiedBy(task));

        task.onlyIf(TestUtil.toClosure("{ task -> false }"));
        assertFalse(task.getOnlyIf().isSatisfiedBy(task));
    }

    @Test
    public void canSpecifyOnlyIfPredicateUsingSpec() {
        final Spec<Task> spec = context.mock(Spec.class);

        final AbstractTask task = getTask();
        assertTrue(task.getOnlyIf().isSatisfiedBy(task));

        context.checking(new Expectations() {{
            allowing(spec).isSatisfiedBy(task);
            will(returnValue(false));
        }});

        task.onlyIf(spec);
        assertFalse(task.getOnlyIf().isSatisfiedBy(task));
    }

    @Test
    public void onlyIfPredicateIsTrueWhenTaskIsEnabledAndAllPredicatesAreTrue() {
        final AtomicBoolean condition1 = new AtomicBoolean(true);
        final AtomicBoolean condition2 = new AtomicBoolean(true);

        AbstractTask task = getTask();
        task.onlyIf(new Spec<Task>() {
            public boolean isSatisfiedBy(Task element) {
                return condition1.get();
            }
        });
        task.onlyIf(new Spec<Task>() {
            public boolean isSatisfiedBy(Task element) {
                return condition2.get();
            }
        });

        assertTrue(task.getOnlyIf().isSatisfiedBy(task));

        task.setEnabled(false);
        assertFalse(task.getOnlyIf().isSatisfiedBy(task));

        task.setEnabled(true);
        condition1.set(false);
        assertFalse(task.getOnlyIf().isSatisfiedBy(task));

        condition1.set(true);
        condition2.set(false);
        assertFalse(task.getOnlyIf().isSatisfiedBy(task));

        condition2.set(true);
        assertTrue(task.getOnlyIf().isSatisfiedBy(task));
    }

    @Test
    public void canReplaceOnlyIfSpec() {
        final AtomicBoolean condition1 = new AtomicBoolean(true);
        AbstractTask task = getTask();
        task.onlyIf(context.mock(Spec.class, "spec1"));
        task.setOnlyIf(new Spec<Task>() {
            public boolean isSatisfiedBy(Task element) {
                return condition1.get();
            }
        });

        assertTrue(task.getOnlyIf().isSatisfiedBy(task));

        task.setEnabled(false);
        assertFalse(task.getOnlyIf().isSatisfiedBy(task));

        task.setEnabled(true);
        condition1.set(false);
        assertFalse(task.getOnlyIf().isSatisfiedBy(task));

        condition1.set(true);
        assertTrue(task.getOnlyIf().isSatisfiedBy(task));
    }
}
