/*
*  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/
package org.ballerinalang.util.codegen;

import org.ballerinalang.util.codegen.cpentries.ConstantPoolEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code ProgramFile} is the runtime representation of a compiled Ballerina program.
 *
 * @since 0.87
 */
public class ProgramFile {
    // TODO Finalize the magic value;
    private int magicValue = 0xBBBBBBBB;

    private short version = (short) 6;

    private List<ConstantPoolEntry> constPool = new ArrayList<>();

    private Map<String, PackageInfo> packageInfoMap = new HashMap<>();

    public int getMagicValue() {
        return magicValue;
    }

    public short getVersion() {
        return version;
    }

    // CP

    public int addCPEntry(ConstantPoolEntry cpEntry) {
        if (constPool.contains(cpEntry)) {
            return constPool.indexOf(cpEntry);
        }

        constPool.add(cpEntry);
        return constPool.size() - 1;
    }

    public int getCPEntryIndex(ConstantPoolEntry cpEntry) {
        return constPool.indexOf(cpEntry);
    }

    public List<ConstantPoolEntry> getConstPool() {
        return constPool;
    }

    // PackageInfo

    public PackageInfo getPackageInfo(String packageName) {
        return packageInfoMap.get(packageName);
    }

    public void addPackageInfo(String packageName, PackageInfo packageInfo) {
        packageInfoMap.put(packageName, packageInfo);
    }
}
