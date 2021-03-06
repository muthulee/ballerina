/**
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import { MENUS as TOOLS_MENUS } from './../tools/constants';
import { MENU_DEF_TYPES } from 'core/menu/constants';
import { MENUS, COMMANDS, LABELS } from './constants';

/**
 * Provides menu definitions of help plugin.
 *
 * @returns {Object[]} menu definitions.
 *
 */
export function getMenuDefinitions(plugin) {
    return [
        {
            id: MENUS.IMPORT_STRUCT_MENU,
            parent: TOOLS_MENUS.TOOLS_MENU,
            label: LABELS.IMPORT_STRUCT_MENU_TITLE,
            isActive: () => {
                const { editor } = plugin.appContext;
                const activeTab = editor.getActiveEditor();
                return activeTab && activeTab.file;
            },
            icon: 'import',
            command: COMMANDS.SHOW_IMPORT_STRUCT_DIALOG,
            type: MENU_DEF_TYPES.ITEM,
        },
    ];
}
