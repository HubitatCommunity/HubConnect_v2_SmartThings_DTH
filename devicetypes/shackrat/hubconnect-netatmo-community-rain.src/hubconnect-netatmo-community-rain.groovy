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
	definition(name: "HubConnect Netatmo Community Rain", namespace: "shackrat", author: "Steve White")
	{
		capability "Sensor"
		capability "Battery"
		capability "Refresh"

		attribute "rain", "number"
		attribute "rainSumHour", "number"
		attribute "rainSumDay", "number"
		attribute "units", "string"
		attribute "lastupdate", "string"
		attribute "version", "string"

		command "sync"
	}

	tiles (scale: 2)
	{
		multiAttributeTile(name:"main", type:"generic", width:6, height:4)
		{
			tileAttribute("rainUnits", key: "PRIMARY_CONTROL")
			{
				attributeState "default",label:'${currentValue}', icon:"st.Weather.weather12", backgroundColor:"#00a0dc"
			}
			tileAttribute ("rainSumHourUnits", key: "SECONDARY_CONTROL")
			{
				attributeState "default", label:'Last Hour: ${currentValue}'
			}
		}
 		valueTile("rainSumDay", "device.rainSumDayUnits", width: 4, height: 1, inactiveLabel: false)
		{
 			state "default", label:'Total Day: ${currentValue}'
 		}
		valueTile("units", "units", width: 2, height: 1, inactiveLabel: false)
		{
 			state "default", label:'Units: ${currentValue}'
 		}
		valueTile("battery", "device.battery", inactiveLabel: false, width: 2, height: 2)
		{
			state "battery_percent", label:'Battery: ${currentValue}%', backgroundColors:[
				[value: 20, color: "#ff0000"],
				[value: 35, color: "#fd4e3a"],
				[value: 50, color: "#fda63a"],
				[value: 60, color: "#fdeb3a"],
				[value: 75, color: "#d4fd3a"],
				[value: 90, color: "#7cfd3a"],
				[value: 99, color: "#55fd3a"]
			]
		}
		valueTile("rain", "device.rain")
		{
 			state "rain",label:'${currentValue}', icon:"st.Weather.weather12", backgroundColor:"#00a0dc"
 		}
		valueTile("lastupdate", "lastupdate", width: 4, height: 1, inactiveLabel: false)
		{
			state "default", label:"Last updated: " + '${currentValue}'
		}
		standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat", width: 2, height: 2)
		{
			state "default", action: "refresh.refresh", icon: "st.secondary.refresh"
		}
		standardTile("sync", "sync", inactiveLabel: false, decoration: "flat", width: 2, height: 2)
		{
			state "default", label: 'Sync', action: "sync", icon: "st.Bath.bath19"
		}
		valueTile("version", "version", inactiveLabel: false, decoration: "flat", width: 2, height: 2)
		{
			state "default", label: '${currentValue}'
		}

		main (["main"])
 		details(["main", "rainSumDay", "battery", "lastupdate", "refresh", "sync", "version"])
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
	parent.syncDevice(device.deviceNetworkId, "netatmowxrain")
	sendEvent([name: "version", value: "v${driverVersion.major}.${driverVersion.minor}.${driverVersion.build}"])
}
