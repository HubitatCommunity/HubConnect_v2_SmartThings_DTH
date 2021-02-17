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
	definition(name: "HubConnect Button", namespace: "shackrat", author: "Steve White", ocfDeviceType: "oic.r.button")
	{
 		capability "Button"				// SmartThings capability
		capability "Temperature Measurement"
		capability "Battery"

		attribute "version", "string"

		command "push"				// SmartThings capability
		command "hold"				// SmartThings capability

		command "sync"
		command "test"
	}

	tiles (scale: 2)
	{
      multiAttributeTile(name:"button", type:"generic", width:6, height:4)
      {
			tileAttribute("device.button", key: "PRIMARY_CONTROL")
			{
				attributeState "default", label:'', backgroundColor:"#ffffff", icon: "st.unknown.zwave.remote-controller"
			}
			tileAttribute ("device.battery", key: "SECONDARY_CONTROL")
			{
				attributeState "battery", label:'${currentValue} % battery'
			}
			tileAttribute("buttons",key: "SECONDARY_CONTROL")
			{
				attributeState "numberOfButtons", label: '${currentValue}'
			}
		}
		standardTile("sync", "sync", inactiveLabel: false, decoration: "flat", width: 2, height: 2)
		{
			state "default", label: 'Sync', action: "sync", icon: "st.Bath.bath19"
		}
		standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat", width: 2, height: 2)
		{
			state "default", action: "refresh.refresh", icon: "st.secondary.refresh"
		}
		valueTile("version", "version", inactiveLabel: false, decoration: "flat", width: 2, height: 2)
		{
			state "default", label: '${currentValue}'
		}

		main(["button"])
		details(["button", "sync", "refresh", "version"])
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

	Doesn't do anything.
*/
def initialize()
{

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
	pushed

	Pushes button #<btn>.
*/
def push(btn)
{
	// The server will update pushed status
	parent.sendDeviceEvent(device.deviceNetworkId, "pushed", [btn])
}


/*
	held

	Holds button #<btn>.
*/
def hold(btn)
{
	// The server will update held status
	parent.sendDeviceEvent(device.deviceNetworkId, "hold", [btn])
}


/*
	doubleTapped

	Double-taps button #<btn>.
*/
def doubleTap(btn)
{
	// The server will update doubleTapped status
	parent.sendDeviceEvent(device.deviceNetworkId, "doubleTap", [btn])
}


/*
	released

	Releases button #<btn>.
*/
def released(btn)
{
	// The server will update released status
	parent.sendDeviceEvent(device.deviceNetworkId, "released", [btn])
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
	parent.syncDevice(device.deviceNetworkId, "button")
	sendEvent([name: "version", value: "v${driverVersion.major}.${driverVersion.minor}.${driverVersion.build}"])
}
