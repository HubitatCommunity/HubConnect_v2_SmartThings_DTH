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
	definition(name: "HubConnect Bulb", namespace: "shackrat", author: "Steve White", ocfDeviceType: "oic.d.light")
	{
		capability "Switch"
		capability "Switch Level"
		capability "Refresh"

		attribute "version", "string"

		command "sync"
	}

	tiles(scale: 2)
	{
	    multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true)
	    {
	        tileAttribute ("device.switch", key: "PRIMARY_CONTROL")
	        {
	            attributeState "on", label:'${name}', action:"switch.off", icon:"st.lights.philips.hue-single", backgroundColor:"#79b821", nextState:"turningOff"
	            attributeState "off", label:'${name}', action:"switch.on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"turningOn"
	            attributeState "turningOn", label:'${name}', action:"switch.off", icon:"st.lights.philips.hue-single", backgroundColor:"#79b821", nextState:"turningOff"
	            attributeState "turningOff", label:'${name}', action:"switch.on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"turningOn"
	        }
	        tileAttribute ("device.level", key: "SLIDER_CONTROL")
	        {
	            attributeState "level", action:"switch level.setLevel"
	        }
	    }
	    standardTile("sync", "sync", inactiveLabel: false, decoration: "flat", width: 2, height: 2)
	    {
		  state "default", label: 'Sync', action: "sync", icon: "st.Bath.bath19"
	    }
	    valueTile("version", "version", inactiveLabel: false, decoration: "flat", width: 2, height: 2)
	    {
		  state "default", label: '${currentValue}'
	    }

	    main(["switch"])
	    details(["switch", "sync", "version"])
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
	on

	Turns the device on.
*/
def on()
{
	// The server will update on/off status
	parent.sendDeviceEvent(device.deviceNetworkId, "on")
}


/*
	off

	Turns the device off.
*/
def off()
{
	// The server will update on/off status
	parent.sendDeviceEvent(device.deviceNetworkId, "off")
}


/*
	setLevel

	Sets the level to <level> over duration <duration>.
*/
def setLevel(value, duration=1)
{
	// The server will respond with a level attribute message.
	parent.sendDeviceEvent(device.deviceNetworkId, "setLevel", [value, duration])
}


/*
	startLevelChange

	Starts changing the level of the bulb in [direction].
*/
def startLevelChange(String direction)
{
	// The server will update status
	parent.sendDeviceEvent(device.deviceNetworkId, "startLevelChange", [direction])
}


/*
	stopLevelChange

	Stops the level change started by startLevelChange.
*/
def stopLevelChange()
{
	// The server will update status
	parent.sendDeviceEvent(device.deviceNetworkId, "stopLevelChange")
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
	parent.syncDevice(device.deviceNetworkId, "rgbbulb")
	sendEvent([name: "version", value: "v${driverVersion.major}.${driverVersion.minor}.${driverVersion.build}"])
}
