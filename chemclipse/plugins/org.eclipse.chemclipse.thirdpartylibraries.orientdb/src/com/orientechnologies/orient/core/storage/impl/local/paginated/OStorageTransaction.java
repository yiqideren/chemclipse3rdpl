/*
 * Copyright 2010-2013 Orient Technologies LTD (info--at--orientechnologies.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orientechnologies.orient.core.storage.impl.local.paginated;

import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.OLogSequenceNumber;
import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.OOperationUnitId;
import com.orientechnologies.orient.core.tx.OTransaction;

/**
 * @author Andrey Lomakin
 * @since 12.06.13
 */
public class OStorageTransaction {

	private final OTransaction clientTx;

	public OStorageTransaction(OTransaction clientTx) {

		this.clientTx = clientTx;
	}

	public OTransaction getClientTx() {

		return clientTx;
	}
}
