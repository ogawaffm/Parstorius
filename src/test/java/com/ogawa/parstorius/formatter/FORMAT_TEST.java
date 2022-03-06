/*
 * Copyright (c) 2020-2022 Kai Bächle
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.ogawa.parstorius.formatter;

import com.ogawa.parstorius.PARSE_RESULT_CAUSE;

public enum FORMAT_TEST {
    // in logical order
    FORMAT_OF_NULL_DEFAULT { public PARSE_RESULT_CAUSE getResultCause() { return PARSE_RESULT_CAUSE.PARSE_OF_NULL; } },
    PARSE_MISSING_DEFAULT { public PARSE_RESULT_CAUSE getResultCause() { return PARSE_RESULT_CAUSE.MISSING_VALUE; } },
    PARSE_NULL_TEXT_DEFAULT { public PARSE_RESULT_CAUSE getResultCause() { return PARSE_RESULT_CAUSE.NULL_AS_TEXT; } },
    PARSE_ERROR_DEFAULT { public PARSE_RESULT_CAUSE getResultCause() { return PARSE_RESULT_CAUSE.ERROR; } },
    PARSE_TEXT_VALUE { public PARSE_RESULT_CAUSE getResultCause() { return PARSE_RESULT_CAUSE.TEXT_VALUE; } };
    public int getDefault() { return -ordinal() - 1; } // -1 ..-n
    abstract public PARSE_RESULT_CAUSE getResultCause();
}
