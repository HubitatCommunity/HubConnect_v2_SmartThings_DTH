/*
 *	Copyright 2019-2020 Steve White, Retail Media Concepts LLC.
 *
 *	Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *	use this file except in compliance with the License. You may obtain a copy
 *	of the License at:
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *	WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *	License for the specific language governing permissions and limitations
 *	under the License.
 *
 *
 */
def getDriverVersion() {[platform: "SmartThings", major: 2, minor: 0, build: 0]}

metadata
{
	definition (name: "HubConnect Iris Z-Wave Repeater", namespace: "shackrat", author: "Steve White")
	{
		capability "Refresh"
		capability "Sensor"
		capability "Configuration"

		command "interrogateDevice"
		command "sync"

		attribute "status", "string"
		attribute "lastRefresh", "string"
		attribute "deviceMSR", "string"
		attribute "lastMsgRcvd","string"
	}

	tiles(scale: 2)
    {
		multiAttributeTile(name: "status", type: "generic", width: 6, height: 4)
		{
			tileAttribute("device.status", key: "PRIMARY_CONTROL")
			{
				attributeState "unknown", label: 'unknown', icon: "st.motion.motion.inactive", backgroundColor: "#ffffff"
				attributeState "online", label: 'online', icon: "st.motion.motion.active", backgroundColor: "#00A0DC"
				attributeState "offline", label: 'offline', icon: "st.motion.motion.inactive", backgroundColor: "#ffffff"
			}
		}

		valueTile("powerLevel", "powerLevel", width: 3, height: 1, inactiveLabel: false, decoration: "flat")
		{
			state "default", label:'POWER LEVEL\n${currentValue}'
		}
		valueTile("deviceVersion", "deviceVersion", width: 3, height: 1, inactiveLabel: false, decoration: "flat")
		{
			state "default", label:'VERSION\n${currentValue}'
		}
		valueTile("deviceMSR", "deviceMSR", width: 3, height: 1, inactiveLabel: false, decoration: "flat")
		{
			state "default", label:'MSR\n${currentValue}'
		}
		valueTile("assocGroup", "assocGroup", width: 3, height: 1, inactiveLabel: false, decoration: "flat")
		{
			state "default", label:'ASSOCIATION\n${currentValue}'
		}
		standardTile("refresh", "device.refresh", width: 3, height: 1, inactiveLabel: false, decoration: "flat")
		{
			state "default", label:'REFRESH', action:"refresh", icon:"st.secondary.refresh"
		}
		standardTile("configure", "device.reconfigure", width: 3, height: 1, inactiveLabel: false, decoration: "flat")
		{
			state "default", label:'Configure', action:"configure", icon:"st.motion.motion.inactive"
		}
		standardTile("testCommunication", "testCommunication", width: 3, height: 1, inactiveLabel: false, decoration: "flat")
		{
			state "default", label:'Z-Wave Test', action:"testCommunication", icon:"st.Entertainment.entertainment15"
		}
		standardTile("interrogateDevice", "interrogateDevice", width: 3, height: 1, inactiveLabel: false, decoration: "flat")
		{
			state "default", label:'Interrogate', action:"interrogateDevice", icon:"st.Office.office9"
		}
		valueTile("lastTestLbl", "lastTestLbl", width: 2, height: 1, inactiveLabel: false, decoration: "flat")
		{
			state "default", label:'Status:'
		}
		valueTile("lastTest", "lastTest", width: 4, height: 1, inactiveLabel: false, decoration: "flat")
		{
			state "default", label:'${currentValue}'
		}
		valueTile("lastRefreshLbl", "lastRefreshLbl", width: 2, height: 1, inactiveLabel: false, decoration: "flat")
		{
			state "default", label:'Last Refresh:'
		}
		valueTile("lastRefresh", "lastRefresh", width: 4, height: 1, inactiveLabel: false, decoration: "flat")
		{
			state "default", label:'${currentValue}'
		}
		valueTile("lastMsgRcvdLbl", "lastMsgRcvdLbl", width: 2, height: 1, inactiveLabel: false, decoration: "flat")
		{
			state "default", label:'Last Response:'
		}
		valueTile("lastMsgRcvd", "lastMsgRcvd", width: 4, height: 1, inactiveLabel: false, decoration: "flat")
		{
			state "default", label:'${currentValue}'
		}
		standardTile("sync", "sync", inactiveLabel: false, decoration: "flat", width: 2, height: 2)
		{
			state "default", label: 'Sync', action: "sync", icon: "st.Bath.bath19"
		}

		main "status"
		details ([
			"status", "powerLevel", "deviceVersion", "deviceMSR", "assocGroup", "lastTestLbl", "lastTest", "lastMsgRcvdLbl",
			"lastMsgRcvd", "lastRefreshLbl", "lastRefresh", "refresh", "interrogateDevice", "configure", "testCommunication", "sync"
		])
	}


}


/*
	installed

	Doesn't do much other than call initialize().
*/
def installed()
{
	initialize()
}


/*
	updated

	Doesn't do much other than call initialize().
*/
def updated()
{
	initialize()
}


/*
	initialize

	Doesn't do much other than call refresh().
*/
def initialize()
{
	refresh()
}


/*
	uninstalled

	Reports to the remote that this device is being uninstalled.
*/
def uninstalled()
{
	// Report
	parent?.sendDeviceEvent(device.deviceNetworkId, "uninstalled")
}


/*
	parse

	In a virtual world this should never be called.
*/
def parse(String description)
{
	log.trace "Msg: Description is $description"
}


/*
	interrogateDevice

	Instructs the driver to interrogate the physical device.
*/
def interrogateDevice()
{
	// The server will update each message received.
	parent.sendDeviceEvent(device.deviceNetworkId, "interrogateDevice")
}


/*
	refresh

	Refreshes the device by requesting an update from the client hub.
*/
def refresh()
{
	// The server will update status
	parent.sendDeviceEvent(device.deviceNetworkId, "refresh")
}


/*
	sync

	Synchronizes the device details with the parent.
*/
def sync()
{
	// The server will respond with updated status and details
	parent.syncDevice(device.deviceNetworkId, "zwaverepeater")
}
