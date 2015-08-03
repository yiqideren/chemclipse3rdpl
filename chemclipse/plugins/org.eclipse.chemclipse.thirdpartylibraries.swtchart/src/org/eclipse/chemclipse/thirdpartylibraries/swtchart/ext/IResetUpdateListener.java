/*******************************************************************************
 * Copyright (c) 2014, 2015 Dr. Philip Wenig.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.thirdpartylibraries.swtchart.ext;

public interface IResetUpdateListener {

	/**
	 * Resets the selection to 1:1.
	 */
	void adjustRange();

	void adjustXRange();

	void adjustYRange();

	void adjustPreviousRange();
}
