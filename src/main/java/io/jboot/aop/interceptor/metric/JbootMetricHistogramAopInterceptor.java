/**
 * Copyright (c) 2015-2019, Michael Yang 杨福海 (fuhai999@gmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jboot.aop.interceptor.metric;


import com.codahale.metrics.Histogram;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import io.jboot.Jboot;
import io.jboot.support.metric.annotation.EnableMetricHistogram;
import io.jboot.kits.ClassKits;
import io.jboot.kits.StringKits;

/**
 * 用于在AOP拦截，并通过Metrics的Hsitogram进行统计
 */
public class JbootMetricHistogramAopInterceptor implements Interceptor {

    private static final String suffix = ".histogram";

    @Override
    public void intercept(Invocation inv) {

        EnableMetricHistogram annotation = inv.getMethod().getAnnotation(EnableMetricHistogram.class);

        if (annotation == null) {
            inv.invoke();
            return;
        }

        Class targetClass = ClassKits.getUsefulClass(inv.getTarget().getClass());
        String name = StringKits.isBlank(annotation.value())
                ? targetClass + "." + inv.getMethod().getName() + suffix
                : annotation.value();


        Histogram histogram = Jboot.getMetric().histogram(name);
        histogram.update(annotation.update());
        inv.invoke();
    }
}
