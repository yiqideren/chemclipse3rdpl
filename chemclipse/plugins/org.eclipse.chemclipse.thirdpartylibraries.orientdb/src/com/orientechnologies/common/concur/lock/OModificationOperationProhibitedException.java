/*
 * * Copyright 2014 Orient Technologies LTD (info(at)orientechnologies.com)
 * *
 * * Licensed under the Apache License, Version 2.0 (the "License");
 * * you may not use this file except in compliance with the License.
 * * You may obtain a copy of the License at
 * *
 * * http://www.apache.org/licenses/LICENSE-2.0
 * *
 * * Unless required by applicable law or agreed to in writing, software
 * * distributed under the License is distributed on an "AS IS" BASIS,
 * * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * * See the License for the specific language governing permissions and
 * * limitations under the License.
 * *
 * * For more information: http://www.orientechnologies.com
 */
package com.orientechnologies.common.concur.lock;

import com.orientechnologies.common.exception.OException;

/**
 * Exception is thrown in case DB is locked for modifications but modification request ist trying to be acquired.
 * 
 * @author Andrey Lomakin
 * @since 03.07.12
 */
public class OModificationOperationProhibitedException extends OException {

	private static final long serialVersionUID = 1L;

	public OModificationOperationProhibitedException() {

	}

	public OModificationOperationProhibitedException(String message) {

		super(message);
	}
}
