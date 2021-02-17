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
	definition(name: "HubConnect Netatmo Community Basestation", namespace: "shackrat", author: "Steve White", cstHandler: true)
	{
		capability "Carbon Dioxide Measurement"
		capability "Relative Humidity Measurement"
		capability "Sound Pressure Level"
		capability "Sound Sensor"
		capability "Temperature Measurement"
		capability "Refresh"

		attribute "pressure", "number"
		attribute "min_temp", "number"
		attribute "max_temp", "number"
		attribute "temp_trend", "string"
		attribute "pressure_trend", "string"
		attribute "lastupdate", "string"
		attribute "version", "string"

		command "sync"
	}

	tiles (scale: 2)
	{
		multiAttributeTile(name:"main", type:"generic", width:6, height:4)
		{
			tileAttribute("temperature", key: "PRIMARY_CONTROL")
			{
				attributeState "temperature",label:'${currentValue}°', icon:"st.Weather.weather2", backgroundColors:[
					[value: 32, color: "#153591"],
					[value: 44, color: "#1e9cbb"],
					[value: 59, color: "#90d2a7"],
					[value: 74, color: "#44b621"],
					[value: 84, color: "#f1d801"],
					[value: 92, color: "#d04e00"],
					[value: 98, color: "#bc2323"]
				]
			}
			tileAttribute ("humidity", key: "SECONDARY_CONTROL")
			{
				attributeState "humidity", label:'Humidity: ${currentValue}%'
			}
		}
		valueTile("temperature", "device.temperature")
		{
 			state("temperature", label: '${currentValue}°', icon:"st.Weather.weather2", backgroundColors: [
 				[value: 31, color: "#153591"],
 				[value: 44, color: "#1e9cbb"],
 				[value: 59, color: "#90d2a7"],
 				[value: 74, color: "#44b621"],
 				[value: 84, color: "#f1d801"],
 				[value: 95, color: "#d04e00"],
 				[value: 96, color: "#bc2323"]
 				]
 				)
 		}
		valueTile("min_temp", "min_temp", width: 2, height: 1)
		{
 			state "min_temp", label: 'Min: ${currentValue}°'
 		}
		valueTile("max_temp", "max_temp", width: 2, height: 1)
		{
 			state "max_temp", label: 'Max: ${currentValue}°'
 		}
 		valueTile("humidity", "device.humidity", inactiveLabel: false)
		{
 			state "humidity", label:'${currentValue}%'
 		}
		valueTile("temp_trend", "temp_trend", width: 4, height: 1)
		{
 			state "temp_trend", label: 'Temp Trend: ${currentValue}'
 		}
		valueTile("pressure_trend", "pressure_trend", width: 4, height: 1)
		{
 			state "pressure_trend", label: 'Press Trend: ${currentValue}'
 		}
 		valueTile("carbonDioxide", "device.carbonDioxide", width: 2, height: 2, inactiveLabel: false)
		{
 			state "carbonDioxide", label:'${currentValue}ppm', backgroundColors: [
 				[value: 600, color: "#44B621"],
				[value: 999, color: "#ffcc00"],
				[value: 1000, color: "#e86d13"]
 				]
 		}
 		valueTile("soundPressureLevel", "device.soundPressureLevel", width: 2, height: 1, inactiveLabel: false)
		{
 			state "soundPressureLevel", label:'${currentValue}db'
		}
 		standardTile("sound", "device.sound", width: 2, height: 1, inactiveLabel: false)
		{
 			state "not detected", label:'Quiet', icon: "st.Entertainment.entertainment15"
			state "detected", label:'Sound detected', icon: "st.Entertainment.entertainment15"
		}
 		valueTile("pressure", "device.pressure", width: 2, height: 1, inactiveLabel: false)
		{
 			state "pressure", label:'${currentValue}'
		}
		valueTile("units", "units", width: 2, height: 1, inactiveLabel: false)
		{
 			state "default", label:'${currentValue}'
		}
		valueTile("lastupdate", "lastupdate", width: 4, height: 1, inactiveLabel: false)
		{
			state "default", label:"Last updated: " + '${currentValue}'
		}
 		valueTile("date_min_temp", "date_min_temp", width: 2, height: 1, inactiveLabel: false)
		{
		  state "default", label:'${currentValue}'
		}
		valueTile("date_max_temp", "date_max_temp", width: 2, height: 1, inactiveLabel: false)
		{
		  state "default", label:'${currentValue}'
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

		main(["main"])
 		details(["main","min_temp","date_min_temp","carbonDioxide", "max_temp","date_max_temp", "temp_trend","sound", "pressure", "units", "soundPressureLevel", "pressure_trend", "refresh", "lastupdate", "sync", "battery"])
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
	parent.syncDevice(device.deviceNetworkId, "netatmowxbase")
	sendEvent([name: "version", value: "v${driverVersion.major}.${driverVersion.minor}.${driverVersion.build}"])
}
