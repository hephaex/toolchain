/*
 * Copyright 2013 the original author or authors.
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

package org.gradle.tooling.internal.consumer;

import com.google.common.base.Preconditions;
import org.gradle.tooling.CancellationToken;
import org.gradle.tooling.LongRunningOperation;
import org.gradle.tooling.ProgressListener;
import org.gradle.tooling.internal.consumer.parameters.ConsumerOperationParameters;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractLongRunningOperation<T extends LongRunningOperation> implements LongRunningOperation {
    protected final ConnectionParameters connectionParameters;
    protected final ConsumerOperationParameters.Builder operationParamsBuilder;

    protected AbstractLongRunningOperation(ConnectionParameters parameters) {
        connectionParameters = parameters;
        operationParamsBuilder = ConsumerOperationParameters.builder();
        operationParamsBuilder.setCancellationToken(new DefaultCancellationTokenSource().token());
    }

    protected abstract T getThis();

    protected final ConsumerOperationParameters getConsumerOperationParameters() {
        ConnectionParameters connectionParameters = this.connectionParameters;
        return operationParamsBuilder.setParameters(connectionParameters).build();
    }

    public T withArguments(String... arguments) {
        operationParamsBuilder.setArguments(arguments);
        return getThis();
    }

    public T setStandardOutput(OutputStream outputStream) {
        operationParamsBuilder.setStdout(outputStream);
        return getThis();
    }

    public T setStandardError(OutputStream outputStream) {
        operationParamsBuilder.setStderr(outputStream);
        return getThis();
    }

    public T setStandardInput(InputStream inputStream) {
        operationParamsBuilder.setStdin(inputStream);
        return getThis();
    }

    public T setJavaHome(File javaHome) {
        operationParamsBuilder.setJavaHome(javaHome);
        return getThis();
    }

    public T setJvmArguments(String... jvmArguments) {
        operationParamsBuilder.setJvmArguments(jvmArguments);
        return getThis();
    }

    public T addProgressListener(ProgressListener listener) {
        operationParamsBuilder.addProgressListener(listener);
        return getThis();
    }

    public LongRunningOperation withCancellationToken(CancellationToken cancellationToken) {
        operationParamsBuilder.setCancellationToken(Preconditions.checkNotNull(cancellationToken));
        return getThis();
    }
}
