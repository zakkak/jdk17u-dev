/*
 * Copyright (c) 2022, Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2022, Red Hat Inc. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/**
 * @test
 * @requires vm.jvmci
 * @library /test/lib /
 * @modules jdk.internal.vm.ci/jdk.vm.ci.hotspot
 *          jdk.internal.vm.ci/jdk.vm.ci.meta
 *          jdk.internal.vm.ci/jdk.vm.ci.runtime
 * @run main/othervm -XX:+UnlockExperimentalVMOptions -XX:+EnableJVMCI -XX:-UseJVMCICompiler compiler.jvmci.meta.MetaUtilInternalNameConversionsTest
 */

package compiler.jvmci.meta;

import static jdk.vm.ci.meta.MetaUtil.internalNameToJava;
import static jdk.vm.ci.meta.MetaUtil.toInternalName;

import java.util.function.Supplier;

import jdk.test.lib.Asserts;
import jdk.vm.ci.hotspot.HotSpotResolvedJavaType;
import jdk.vm.ci.meta.MetaAccessProvider;
import jdk.vm.ci.runtime.JVMCI;

public class MetaUtilInternalNameConversionsTest {
    static final Supplier<String> aSupplier = () -> "I am a lambda method";

    public static void main(String[] args) throws Throwable {
        MetaAccessProvider metaAccess = JVMCI.getRuntime().getHostJVMCIBackend().getMetaAccess();
        HotSpotResolvedJavaType type = (HotSpotResolvedJavaType) metaAccess.lookupJavaType(aSupplier.getClass());
        String internalName = type.getName();
        String javaName = aSupplier.getClass().getName();

        Asserts.assertStringsEqual(internalName, toInternalName(javaName),
                "MetaUtil.toInternalName returns different result from HotSpotResolvedJavaType.getName()");
        Asserts.assertStringsEqual(javaName, internalNameToJava(internalName, true, true),
                "MetaUtil.internalNameToJava returns different result from Class.getName()");
    }
}
