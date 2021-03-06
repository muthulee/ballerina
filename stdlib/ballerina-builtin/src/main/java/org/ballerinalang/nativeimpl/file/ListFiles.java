/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.nativeimpl.file;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.BlockingNativeCallableUnit;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BRefType;
import org.ballerinalang.model.values.BRefValueArray;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.ballerinalang.natives.annotations.ReturnType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.ballerinalang.nativeimpl.file.utils.FileUtils.createAccessDeniedError;
import static org.ballerinalang.nativeimpl.file.utils.FileUtils.createFileStruct;
import static org.ballerinalang.nativeimpl.file.utils.FileUtils.createIOError;

/**
 * Lists the files in the specified directory.
 *
 * @since 0.94.1
 */
@BallerinaFunction(
        packageName = "ballerina.file",
        functionName = "list",
        receiver = @Receiver(type = TypeKind.STRUCT, structType = "File", structPackage = "ballerina.file"),
        returnType = {@ReturnType(type = TypeKind.ARRAY, elementType = TypeKind.STRUCT),
                @ReturnType(type = TypeKind.STRUCT), @ReturnType(type = TypeKind.STRUCT)},
        isPublic = true
)
public class ListFiles extends BlockingNativeCallableUnit {

    private static final Logger log = LoggerFactory.getLogger(ListFiles.class);

    @Override
    public void execute(Context context) {
        BStruct fileStruct = (BStruct) context.getRefArgument(0);
        String path = fileStruct.getStringField(0);
        List<BStruct> structList = new ArrayList<>();
        BRefValueArray filesList;

        try {
            Files.list(Paths.get(path)).forEach(p -> structList.add(createFileStruct(context, p.toString())));
            filesList = new BRefValueArray(structList.toArray(new BRefType[0]), fileStruct.getType());
            context.setReturnValues(filesList, null, null);
        } catch (IOException e) {
            String msg = "Error occurred while opening directory: " + path;
            log.error(msg, e);
            context.setReturnValues(null, null, createIOError(context, msg));
        } catch (SecurityException e) {
            String msg = "Permission denied. Could not open directory: " + path;
            log.error(msg, e);
            context.setReturnValues(null, createAccessDeniedError(context, msg), null);
        }
    }
}
