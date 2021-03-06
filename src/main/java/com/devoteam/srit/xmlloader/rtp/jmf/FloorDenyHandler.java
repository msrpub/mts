/* 
 * Copyright 2012 Devoteam http://www.devoteam.com
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * 
 * This file is part of Multi-Protocol Test Suite (MTS).
 * 
 * Multi-Protocol Test Suite (MTS) is free software: you can redistribute
 * it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the
 * License.
 * 
 * Multi-Protocol Test Suite (MTS) is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Multi-Protocol Test Suite (MTS).
 * If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.devoteam.srit.xmlloader.rtp.jmf;

import javax.media.rtp.event.ApplicationEvent;


/**
 * A class to handle floor deny message
 */
class FloorDenyHandler {
    private FloorDenyHandler() {
        // Avoid instanciation
    }
    /**
     * handle floor deny message <code>
     *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1  
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |V=2|P|0 0 0 1 1|   PT=APP=204  |            length             |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                      SSRC of PoC server                       |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                          name=PoC1                            |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |  Reason code  |    Length     |         Reason Phrase         |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+                               :
     * :                                                               :
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * @param ctrl controler
     * @param evt the event
     */
    protected final static void handleFloorDeny(FloorController ctrl,
            ApplicationEvent evt) {
        // Floor request has been denied by the PoC server
        // -----------------------------------------------

        // Stop retransmission timer
        ctrl.timerT0.stop();

        // Notify listeners
        ctrl.notifyFloorListeners(new FloorEvent(FloorEvent.EVENT_FLOOR_DENY,
                extractDenyReason(evt.getAppData())));
    }
    
	/**
	 * Extract the reason phrase of the floor DENY event
	 * 
	 * @param data Application data
	 * @return Reason phrase
	 */
	private static String extractDenyReason(byte[] data) {
		//  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
		// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
		// |  Reason code  |    Length     |         Reason Phrase         |
		// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+                               :
		// :                                                               :
		// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
		String reason = "";
		try {
			int length = data[1];
			if (length != 0) {
				for(int i=2; i < (length + 2); i++) {
					String car = new Character((char)data[i]).toString();		
					reason = reason + car;
				}		
			}
		} catch(Exception e) {
			reason = "unknown";
		}
		return reason;
	}
}
