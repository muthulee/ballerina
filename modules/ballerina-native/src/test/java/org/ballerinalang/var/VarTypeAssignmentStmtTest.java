/*
*  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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


package org.ballerinalang.var;

import org.ballerinalang.model.values.BBoolean;
import org.ballerinalang.model.values.BFloat;
import org.ballerinalang.model.values.BInteger;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.nativeimpl.util.BTestUtils;
import org.ballerinalang.util.codegen.ProgramFile;
import org.ballerinalang.util.program.BLangFunctions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Var type assignment statement test.
 *
 * @since 0.88
 */
public class VarTypeAssignmentStmtTest {

    private ProgramFile bLangProgram;

    @BeforeClass
    public void setup() {
        bLangProgram = BTestUtils.getProgramFile("lang/var/var-type-assign-stmt.bal");
    }

    @Test
    public void testIntToVarAssignment() {
        BValue[] returns = BLangFunctions.invokeNew(bLangProgram, "testIntToVarAssignment",
                new BValue[]{});
        Assert.assertEquals(((BInteger)returns[0]).intValue(), 81);
    }

    @Test
    public void testStringToVarAssignment() {
        BValue[] returns = BLangFunctions.invokeNew(bLangProgram, "testStringToVarAssignment",
                new BValue[]{});
        Assert.assertEquals(((BString)returns[0]).stringValue(), "kevin");
    }

    @Test
    public void testBooleanToVarAssignment() {
        BValue[] returns = BLangFunctions.invokeNew(bLangProgram, "testBooleanToVarAssignment",
                new BValue[]{});
        Assert.assertEquals(((BBoolean)returns[0]).booleanValue(), true);
    }

    @Test
    public void testIncompatibleJsonToStructWithErrors() {
        BValue[] returns = BLangFunctions.invokeNew(bLangProgram, "testIncompatibleJsonToStructWithErrors",
                new BValue[]{});
        Assert.assertNull(returns[0]);
        Assert.assertTrue(returns[1] instanceof BStruct);
        BStruct error = (BStruct) returns[1];
        String errorMsg = error.getStringField(0);
        Assert.assertEquals(errorMsg, "cannot convert 'json' to type 'Person': error while mapping" +
                " 'parent': incompatible types: expected 'json-object', found 'string'");
    }


    @Test
    public void testJsonToStructWithErrors() {
        BValue[] returns = BLangFunctions.invokeNew(bLangProgram, "testJsonToStructWithErrors",
                new BValue[]{});
        Assert.assertNull(returns[0]);
        Assert.assertTrue(returns[1] instanceof BStruct);
        BStruct error = (BStruct) returns[1];
        String errorMsg = error.getStringField(0);
        Assert.assertEquals(errorMsg, "cannot convert 'json' to type 'PersonA': " +
                "error while mapping 'age': no such field found");
    }

    @Test
    public void testCompatibleStructForceCasting() {
        BValue[] returns = BLangFunctions.invokeNew(bLangProgram, "testCompatibleStructForceCasting", new BValue[]{});
        Assert.assertTrue(returns[0] instanceof BStruct);
        BStruct structC = (BStruct) returns[0];

        Assert.assertEquals(structC.getStringField(0), "updated-x-valueof-a");

        Assert.assertEquals(structC.getIntField(0), 4);

        // check whether error is null
        Assert.assertNull(returns[1]);
    }

    @Test
    public void testInCompatibleStructForceCasting() {
        BValue[] returns = BLangFunctions.invokeNew(bLangProgram, "testInCompatibleStructForceCasting", new BValue[]{});

        // check whether struct is null
        Assert.assertNull(returns[0]);

        // check the error
        Assert.assertTrue(returns[1] instanceof BStruct);
        BStruct error = (BStruct) returns[1];
        String errorMsg = error.getStringField(0);
        Assert.assertEquals(errorMsg, "'B' cannot be cast to 'A'");

        String sourceType = error.getStringField(1);
        Assert.assertEquals(sourceType, "B");

        String targetType = error.getStringField(2);
        Assert.assertEquals(targetType, "A");
    }

    @Test
    public void testAnyToStringWithErrors() {
        BValue[] returns = BLangFunctions.invokeNew(bLangProgram, "testAnyToStringWithErrors", new BValue[]{});

        // check whether string is empty
        Assert.assertEquals(returns[0].stringValue(), "");

        // check the error
        Assert.assertTrue(returns[1] instanceof BStruct);
        BStruct error = (BStruct) returns[1];
        String errorMsg = error.getStringField(0);
        Assert.assertEquals(errorMsg, "'int' cannot be cast to 'string'");
    }

    @Test
    public void testAnyNullToStringWithErrors() {
        BValue[] returns = BLangFunctions.invokeNew(bLangProgram, "testAnyNullToStringWithErrors", new BValue[]{});

        // check whether string is empty
        Assert.assertEquals(returns[0].stringValue(), "");

        // check the error
        Assert.assertTrue(returns[1] instanceof BStruct);
        BStruct error = (BStruct) returns[1];
        String errorMsg = error.getStringField(0);
        Assert.assertEquals(errorMsg, "'null' cannot be cast to 'string'");
    }

    @Test
    public void testAnyToBooleanWithErrors() {
        BValue[] returns = BLangFunctions.invokeNew(bLangProgram, "testAnyToBooleanWithErrors", new BValue[]{});

        // check whether string is empty
        Assert.assertEquals(((BBoolean) returns[0]).booleanValue(), false);

        // check the error
        Assert.assertTrue(returns[1] instanceof BStruct);
        BStruct error = (BStruct) returns[1];
        String errorMsg = error.getStringField(0);
        Assert.assertEquals(errorMsg, "'int' cannot be cast to 'boolean'");
    }

    @Test
    public void testAnyNullToBooleanWithErrors() {
        BValue[] returns = BLangFunctions.invokeNew(bLangProgram, "testAnyNullToBooleanWithErrors", new BValue[]{});

        // check whether string is empty
        Assert.assertEquals(((BBoolean) returns[0]).booleanValue(), false);

        // check the error
        Assert.assertTrue(returns[1] instanceof BStruct);
        BStruct error = (BStruct) returns[1];
        String errorMsg = error.getStringField(0);
        Assert.assertEquals(errorMsg, "'null' cannot be cast to 'boolean'");
    }

    @Test
    public void testAnyToIntWithErrors() {
        BValue[] returns = BLangFunctions.invokeNew(bLangProgram, "testAnyToIntWithErrors", new BValue[]{});

        // check whether int is zero
        Assert.assertEquals(((BInteger) returns[0]).intValue(), 0);

        // check the error
        Assert.assertTrue(returns[1] instanceof BStruct);
        BStruct error = (BStruct) returns[1];
        String errorMsg = error.getStringField(0);
        Assert.assertEquals(errorMsg, "'string' cannot be cast to 'int'");
    }

    @Test
    public void testAnyNullToIntWithErrors() {
        BValue[] returns = BLangFunctions.invokeNew(bLangProgram, "testAnyNullToIntWithErrors", new BValue[]{});

        // check whether int is zero
        Assert.assertEquals(((BInteger) returns[0]).intValue(), 0);

        // check the error
        Assert.assertTrue(returns[1] instanceof BStruct);
        BStruct error = (BStruct) returns[1];
        String errorMsg = error.getStringField(0);
        Assert.assertEquals(errorMsg, "'null' cannot be cast to 'int'");
    }

    @Test
    public void testAnyToFloatWithErrors() {
        BValue[] returns = BLangFunctions.invokeNew(bLangProgram, "testAnyToFloatWithErrors", new BValue[]{});

        // check whether float is zero
        Assert.assertEquals(((BFloat) returns[0]).floatValue(), 0.0);

        // check the error
        Assert.assertTrue(returns[1] instanceof BStruct);
        BStruct error = (BStruct) returns[1];
        String errorMsg = error.getStringField(0);
        Assert.assertEquals(errorMsg, "'string' cannot be cast to 'float'");
    }

    @Test
    public void testAnyNullToFloatWithErrors() {
        BValue[] returns = BLangFunctions.invokeNew(bLangProgram, "testAnyNullToFloatWithErrors", new BValue[]{});

        // check whether float is zero
        Assert.assertEquals(((BFloat) returns[0]).floatValue(), 0.0);

        // check the error
        Assert.assertTrue(returns[1] instanceof BStruct);
        BStruct error = (BStruct) returns[1];
        String errorMsg = error.getStringField(0);
        Assert.assertEquals(errorMsg, "'null' cannot be cast to 'float'");
    }

    @Test
    public void testAnyToMapWithErrors() {
        BValue[] returns = BLangFunctions.invokeNew(bLangProgram, "testAnyToMapWithErrors", new BValue[]{});

        // check whether map is null
        Assert.assertNull(returns[0]);

        // check the error
        Assert.assertTrue(returns[1] instanceof BStruct);
        BStruct error = (BStruct) returns[1];
        String errorMsg = error.getStringField(0);
        Assert.assertEquals(errorMsg, "'string' cannot be cast to 'map'");
    }


}
