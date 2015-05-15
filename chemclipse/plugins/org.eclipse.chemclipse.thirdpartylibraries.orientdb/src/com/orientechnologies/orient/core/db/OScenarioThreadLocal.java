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
package com.orientechnologies.orient.core.db;

import com.orientechnologies.orient.core.OOrientListenerAbstract;
import com.orientechnologies.orient.core.OOrientShutdownListener;
import com.orientechnologies.orient.core.OOrientStartupListener;
import com.orientechnologies.orient.core.Orient;
import com.orientechnologies.orient.core.db.OScenarioThreadLocal.RUN_MODE;

/**
 * Thread local to know when the request comes from distributed requester avoiding loops.
 * 
 * @author Luca Garulli (l.garulli--at--orientechnologies.com)
 * 
 */
public class OScenarioThreadLocal extends ThreadLocal<RUN_MODE> {

	public static volatile OScenarioThreadLocal INSTANCE = new OScenarioThreadLocal();
	static {
		Orient.instance().registerListener(new OOrientListenerAbstract() {

			@Override
			public void onStartup() {

				if(INSTANCE == null)
					INSTANCE = new OScenarioThreadLocal();
			}

			@Override
			public void onShutdown() {

				INSTANCE = null;
			}
		});
	}

	public enum RUN_MODE {
		DEFAULT, RUNNING_DISTRIBUTED
	}

	public OScenarioThreadLocal() {

		set(RUN_MODE.DEFAULT);
	}

	@Override
	public void set(final RUN_MODE value) {

		super.set(value);
	}

	@Override
	public RUN_MODE get() {

		RUN_MODE result = super.get();
		if(result == null)
			result = RUN_MODE.DEFAULT;
		return result;
	}
}
