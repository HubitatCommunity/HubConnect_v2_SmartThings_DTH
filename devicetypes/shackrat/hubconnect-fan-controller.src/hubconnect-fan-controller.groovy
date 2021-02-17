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
	definition(name: "HubConnect Fan Controller", namespace: "shackrat", author: "Steve White", ocfDeviceType: "x.com.st.fanspeed")
	{
		capability "Refresh"

		attribute "version", "string"

		command "sync"

		capability "Fan Speed"
		capability "Switch"
		capability "Switch Level"
		command "setOff"
		command "setOn"
		command "setLowSpeed"
		command "setMedLowSpeed"
		command "setMedSpeed"
		command "setMedHiSpeed"
		command "setHighSpeed"
		command "setAutoSpeed"
	}

	tiles (scale:2) {
		multiAttributeTile(name: "fanSpeed", type: "lighting", width: 6, height: 4, canChangeIcon: true)
		{
			tileAttribute ("speed", key: "PRIMARY_CONTROL")
			{
				attributeState "on",  label:'${name}', action:"setOff", icon:"st.Lighting.light24", backgroundColor:"#00A0DC", nextState:"turningOff"
				attributeState "off", label:'${name}', action:"setOn",  icon:"st.Lighting.light24", backgroundColor:"#ffffff", nextState:"turningOn"
				attributeState "turningOn",  label:'${name}', action:"setOn", icon:"st.Lighting.light24", backgroundColor:"#00A0DC", nextState:"turningOn"
				attributeState "turningOff", label:'${name}', action:"setOff", icon:"st.Lighting.light24", backgroundColor:"#ffffff", nextState:"turningOff"
			}
			tileAttribute ("device.fanspeed", key: "SLIDER_CONTROL")
			{
				attributeState "level", action:"setLevel"
			}
		}
		standardTile("lowSpeed", "device.currentState", inactiveLabel: false, width: 2, height: 2)
		{
			state "default", label: 'LOW', action: "setLowSpeed", icon:"st.Appliances.appliances11", backgroundColor: "#ffffff"
			state "LOW", label:'LOW', action: "setLowSpeed", icon:"st.Appliances.appliances11", backgroundColor: "#79b821"
			state "ADJUSTING.LOW", label:'LOW', action: "setLowSpeed", icon:"st.Appliances.appliances11", backgroundColor: "#2179b8"
  		}
		standardTile("medLowSpeed", "device.currentState", inactiveLabel: false, width: 2, height: 2)
		{
			state "default", label: 'MEDLOW', action: "setMedLowSpeed", icon:"st.Appliances.appliances11", backgroundColor: "#ffffff"
			state "MEDLOW", label: 'MEDLOW', action: "setMedLowSpeed", icon:"st.Appliances.appliances11", backgroundColor: "#79b821"
			state "ADJUSTING.MEDLOW", label:'MEDLOW', action: "setMedLowSpeed", icon:"st.Appliances.appliances11", backgroundColor: "#2179b8"
		}
		standardTile("medSpeed", "device.currentState", inactiveLabel: false, width: 2, height: 2)
		{
			state "default", label: 'MED', action: "setMedSpeed", icon:"st.Appliances.appliances11", backgroundColor: "#ffffff"
			state "MED", label: 'MED', action: "setMedSpeed", icon:"st.Appliances.appliances11", backgroundColor: "#79b821"
			state "ADJUSTING.MED", label:'MED', action: "setMedSpeed", icon:"st.Appliances.appliances11", backgroundColor: "#2179b8"
		}
		standardTile("medHiSpeed", "device.currentState", inactiveLabel: false, width: 2, height: 2)
		{
			state "default", label: 'MEDHI', action: "setMedHiSpeed", icon:"st.Appliances.appliances11", backgroundColor: "#ffffff"
			state "MEDHI", label: 'MEDHI', action: "setMedHiSpeed", icon:"st.Appliances.appliances11", backgroundColor: "#79b821"
			state "ADJUSTING.MEDHI", label:'MEDHI', action: "setMedHiSpeed", icon:"st.Appliances.appliances11", backgroundColor: "#2179b8"
		}
		standardTile("highSpeed", "device.currentState", inactiveLabel: false, width: 2, height: 2)
		{
			state "default", label: 'HIGH', action: "setHighSpeed", icon:"st.Appliances.appliances11", backgroundColor: "#ffffff"
			state "HIGH", label: 'HIGH', action: "setHighSpeed", icon:"st.Appliances.appliances11", backgroundColor: "#79b821"
			state "ADJUSTING.HIGH", label:'HIGH', action: "setHighSpeed", icon:"st.Appliances.appliances11", backgroundColor: "#2179b8"
		}
		standardTile("comfort", "device.currentState", inactiveLabel: false, width: 2, height: 2)
		{
			state "default", label: 'AUTO', action: "setAutoSpeed", icon:"st.Appliances.appliances11", backgroundColor: "#ffffff"
			state "AUTO", label: 'AUTO', action: "setAutoSpeed", icon:"st.Appliances.appliances11", backgroundColor: "#79b821"
			state "ADJUSTING.AUTO", label:'AUTO', action: "setAutoSpeed", icon:"st.Appliances.appliances11", backgroundColor: "#2179b8"
		}
		controlTile("levelSliderControl", "device.fanspeed", "slider", width: 2, height: 2, inactiveLabel: false)
		{
			state "level", action:"setLevel"
		}
		standardTile("sync", "sync", inactiveLabel: false, decoration: "flat", width: 2, height: 2)
		{
			state "default", label: 'Sync', action: "sync", icon: "st.Bath.bath19"
		}
		valueTile("version", "version", inactiveLabel: false, decoration: "flat", width: 2, height: 2)
		{
			state "default", label: '${currentValue}'
		}
		standardTile("refresh", "device.fanSpeed", inactiveLabel: false, decoration: "flat", width: 2, height: 2)
		{
			state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
		}

		main(["fanSpeed"])
		details(["fanSpeed", "lowSpeed", "medLowSpeed", "medSpeed", "medHiSpeed", "highSpeed", "comfort", "levelSliderControl", "sync", "refresh", "version"])
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
	setSpeed

	Set enumerated speeds: off, low, medium-low, medium, medium-high, high, auto
*/
def setSpeed(value)
{
	// The server will update status
	parent.sendDeviceEvent(device.deviceNetworkId, "setSpeed", [value])
}


  /*
	setLevel

	Convert level to enumerated speeds: off, low, medium-low, medium, medium-high, high, auto
  */
  def setLevel(value)
  {
  	// .
  	//log.debug "send setLevel: $value"
  	if (value == 0) setSpeed("off")
  	if (value > 0 && value < 20) setSpeed("low")
  	if (value > 20 && value < 40) setSpeed("medium-low")
  	if (value > 40 && value < 60) setSpeed("medium")
  	if (value > 60 && value < 80) setSpeed("medium-high")
  	if (value > 80 && value < 98) setSpeed("high")
  	if (value > 98) setSpeed("auto")
  }

  // helpers
  def setOn()		{ setSpeed("on") }
  def setOff()		{ setSpeed("off") }
  def setLowSpeed()	{ setSpeed("low") }
  def setMedLowSpeed()	{ setSpeed("medium-low") }
  def setMedSpeed()	{ setSpeed("medium") }
  def setMedHiSpeed()	{ setSpeed("medium-high") }
  def setHighSpeed()	{ setSpeed("high") }
  def setAutoSpeed()	{ setSpeed("auto") }


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
	parent.syncDevice(device.deviceNetworkId, "fanControl")
	sendEvent([name: "version", value: "v${driverVersion.major}.${driverVersion.minor}.${driverVersion.build}"])
}
