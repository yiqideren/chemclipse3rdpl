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
package com.orientechnologies.orient.core.storage.impl.local.paginated.wal;

import com.orientechnologies.common.serialization.types.OByteSerializer;

/**
 * @author Andrey Lomakin
 * @since 24.05.13
 */
public class OAtomicUnitEndRecord extends OOperationUnitBodyRecord {

	private boolean rollback;

	public OAtomicUnitEndRecord() {

	}

	OAtomicUnitEndRecord(OOperationUnitId operationUnitId, boolean rollback, OLogSequenceNumber startLsn) {

		super(operationUnitId, startLsn);
		this.rollback = rollback;
		assert operationUnitId != null;
	}

	public boolean isRollback() {

		return rollback;
	}

	@Override
	public int toStream(final byte[] content, int offset) {

		offset = super.toStream(content, offset);
		content[offset] = rollback ? (byte)1 : 0;
		offset++;
		return offset;
	}

	@Override
	public int fromStream(final byte[] content, int offset) {

		offset = super.fromStream(content, offset);
		rollback = content[offset] > 0;
		return offset;
	}

	@Override
	public int serializedSize() {

		return super.serializedSize() + OByteSerializer.BYTE_SIZE;
	}

	@Override
	public boolean isUpdateMasterRecord() {

		return false;
	}

	@Override
	public String toString() {

		return toString("rollback=" + rollback);
	}
}
