/**
 *  Aeon HEM
 *
 *  Copyright 2014 Barry A. Burke
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Aeon Home Energy Meter (US)
 *
 *  Author: Barry A. Burke
 *  Contributors: Brock Haymond: UI updates
 *
 *  Genesys: Based off of Aeon Smart Meter Code sample provided by SmartThings (2013-05-30). Built on US model
 *			 may also work on international versions (currently reports total values only) 
 */
metadata {
	// Automatically generated. Make future change here.
	definition (
		name: 		"Aeon HEMv2+", 
		namespace: 	"Green Living",
		category: 	"Green Living",
		author: 	"Barry A. Burke"
	) 
	{
    	capability "Energy Meter"
		capability "Power Meter"
		capability "Configuration"
		capability "Sensor"
        capability "Refresh"
        capability "Polling"
        
        attribute "energy", "string"
        attribute "power", "string"
        
        attribute "energyDisp", "string" // Total Kwh
        attribute "energyOne", "string"
        attribute "energyTwo", "string"
        
        attribute "powerDisp", "string"  //Current usage in Watts
        attribute "powerOne", "string"
        attribute "powerTwo", "string"    
        
		command "reset"
        command "configure"
        command "refresh"
        command "toggleDisplay"
        
		fingerprint deviceId: "0x2101", inClusters: " 0x70,0x31,0x72,0x86,0x32,0x80,0x85,0x60"
	}

	// simulator metadata
	simulator {
		for (int i = 0; i <= 10000; i += 1000) {
			status "power  ${i} W": new physicalgraph.zwave.Zwave().meterV1.meterReport(
				scaledMeterValue: i, precision: 3, meterType: 33, scale: 2, size: 4).incomingMessage()
		}
		for (int i = 0; i <= 100; i += 10) {
			status "energy  ${i} kWh": new physicalgraph.zwave.Zwave().meterV1.meterReport(
				scaledMeterValue: i, precision: 3, meterType: 33, scale: 0, size: 4).incomingMessage()
		}
	}

	// tile definitions
	tiles {
		valueTile("powerDisp", "device.powerDisp") {
			state (
				"default", 
				label:'${currentValue}', 
            	foregroundColors:[
            		[value: 1, color: "#000000"],
            		[value: 10000, color: "#ffffff"]
            	], 
            	foregroundColor: "#000000",
                backgroundColors:[
					[value: "0", color: "#153591"],
					[value: "500", color: "#1e9cbb"],
					[value: "1000", color: "#90d2a7"],
					[value: "1500", color: "#44b621"],
					[value: "2000", color: "#f1d801"],
					[value: "2500", color: "#d04e00"],
					[value: "3000", color: "#bc2323"]
				]
			)
		}
        valueTile("powerOne", "device.powerOne") {
        	state(
        		"default", 
        		label:'${currentValue}', 
            	foregroundColors:[
            		[value: 1, color: "#000000"],
            		[value: 10000, color: "#ffffff"]
            	], 
            	foregroundColor: "#000000",
                backgroundColors:[
					[value: "0", color: "#153591"],
					[value: "500", color: "#1e9cbb"],
					[value: "1000", color: "#90d2a7"],
					[value: "1500", color: "#44b621"],
					[value: "2000", color: "#f1d801"],
					[value: "2500", color: "#d04e00"],
					[value: "3000", color: "#bc2323"]
				]
			)
        }
        valueTile("powerTwo", "device.powerTwo") {
        	state(
        		"default", 
        		label:'${currentValue}', 
            	foregroundColors:[
            		[value: 1, color: "#000000"],
            		[value: 10000, color: "#ffffff"]
            	], 
            	foregroundColor: "#000000",
                backgroundColors:[
					[value: "0", color: "#153591"],
					[value: "500", color: "#1e9cbb"],
					[value: "1000", color: "#90d2a7"],
					[value: "1500", color: "#44b621"],
					[value: "2000", color: "#f1d801"],
					[value: "2500", color: "#d04e00"],
					[value: "3000", color: "#bc2323"]
				]
			)
        }

		// Total Kwh Energy Usage
		valueTile("energyDisp", "device.energyDisp") {
			state(
				"default", 
				label: '${currentValue}', 
				foregroundColor: "#000000", 
				backgroundColor: "#ffffff")
		}
        valueTile("energyOne", "device.energyOne") {
        	state(
        		"default", 
        		label: '${currentValue}', 
        		foregroundColor: "#000000", 
        		backgroundColor: "#ffffff")
        }        
        valueTile("energyTwo", "device.energyTwo") {
        	state(
        		"default", 
        		label: '${currentValue}', 
        		foregroundColor: "#000000", 
        		backgroundColor: "#ffffff")
        }
    
    	// Controls row
		standardTile("reset", "command.reset", inactiveLabel: false) {
			state "default", label:'reset', action:"reset", icon: "st.Health & Wellness.health7"
		}
		standardTile("refresh", "command.refresh", inactiveLabel: false) {
			state "default", label:'refresh', action:"refresh.refresh", icon:"st.secondary.refresh-icon"
		}
		standardTile("configure", "command.configure", inactiveLabel: false) {
			state "configure", label:'', action: "configure", icon:"st.secondary.configure"
		}
		standardTile("toggle", "command.toggleDisplay", inactiveLabel: false) {
			state "default", label: "toggle", action: "toggleDisplay", icon: "st.motion.motion.inactive"
		}

		// HEM Version Configuration only needs to be done here - comments to choose what gets displayed
		main (["energyDisp","energyTwo", "powerDisp"
		])
		details([
			"energyOne","energyDisp","energyTwo",
			"powerOne","powerDisp","powerTwo",
			"reset","configure","toggle"  // refresh
		])
	}
    preferences {
        input "voltage", "number", title: "Voltage (120)", /* description: "120", */ defaultValue: 120
    	input "kWhCost", "string", title: "\$/kWh (0.1005)", description: "0.1005", defaultValue: "0.1005" as String
    	input "kWhDelay", "number", title: "kWh report seconds (60)", /* description: "120", */ defaultValue: 120
    	input "detailDelay", "number", title: "Detail report seconds (30)", /* description: "30", */ defaultValue: 30
		input "enableLog", "boolean", title: "Enable Logging", /* description: "false", */ defaultValue: false
    }
}

def installed() {
	state.display = 1
	reset()						// The order here is important
	configure()					// Since reports can start coming in even before we finish configure()
	refresh()
}

def updated() {
	configure()
	resetDisplay()
	refresh()
}

def parse(String description) {
	def result = null
	def cmd = zwave.parse(description, [0x31: 1, 0x32: 1, 0x60: 3])
	if (cmd) {
		result = createEvent(zwaveEvent(cmd))
	}
	if (result) { 
		logDebug "Parse returned ${result?.descriptionText}"
		return result
	}
}

def zwaveEvent(physicalgraph.zwave.commands.meterv1.MeterReport cmd) {
    def dispValue
    def newValue
    def formattedValue
    def MAX_AMPS = 220
    def MAX_WATTS = 24000
    
	def timeString = new Date().format("h:mm a", location.timeZone)
    
    if (cmd.meterType == 33) {
		if (cmd.scale == 0) {
        	newValue = (Math.round(cmd.scaledMeterValue * 100) / 100);
            state.energyTotalInDevice = newValue; 
            newValue = newValue - state.energyTotalInDeviceAtReset;
        	if (newValue != state.energyValue) {
        		formattedValue = String.format("%5.2f", newValue)
    			dispValue = "${formattedValue}\nkWh"
                sendEvent(name: "energyDisp", value: dispValue as String, unit: "", descriptionText: "Display Energy: ${newValue} kWh", displayed: false)
                state.energyValue = newValue
                BigDecimal costDecimal = newValue * ( kWhCost as BigDecimal )
                def costDisplay = String.format("%5.2f",costDecimal)
                state.costDisp = "Cost\n\$"+costDisplay
                if (state.display == 1) { sendEvent(name: "energyTwo", value: state.costDisp, unit: "", descriptionText: "Display Cost: \$${costDisp}", displayed: false) }
                [name: "energy", value: newValue, unit: "kWh", descriptionText: "Total Energy: ${formattedValue} kWh"]
            }
		} 
		else if (cmd.scale == 1) {
            newValue = Math.round( cmd.scaledMeterValue * 100) / 100
            if (newValue != state.energyValue) {
            	formattedValue = String.format("%5.2f", newValue)
    			dispValue = "${formattedValue}\nkVAh"
                sendEvent(name: "energyDisp", value: dispValue as String, unit: "", descriptionText: "Display Energy: ${formattedValue} kVAh", displayed: false)
                state.energyValue = newValue
				[name: "energy", value: newValue, unit: "kVAh", descriptionText: "Total Energy: ${formattedValue} kVAh"]
            }
		}
		else if (cmd.scale==2) {				
        	newValue = Math.round(cmd.scaledMeterValue)		// really not worth the hassle to show decimals for Watts
            if (newValue > MAX_WATTS) { return }				// Ignore ridiculous values (a 200Amp supply @ 120volts is roughly 24000 watts)
        	if (newValue != state.powerValue) {
    			dispValue = newValue+"\nWatts"
                sendEvent(name: "powerDisp", value: dispValue as String, unit: "", descriptionText: "Display Power: ${newValue} Watts", displayed: false)
                
                if (newValue < state.powerLow) {
                	dispValue = newValue+"\n"+timeString
                	if (state.display == 1) { sendEvent(name: "powerOne", value: dispValue as String, unit: "", descriptionText: "Lowest Power: ${newValue} Watts")	}
                    state.powerLow = newValue
                    state.powerLowDisp = dispValue
                }
                if (newValue > state.powerHigh) {
                	dispValue = newValue+"\n"+timeString
                	if (state.display == 1) { sendEvent(name: "powerTwo", value: dispValue as String, unit: "", descriptionText: "Highest Power: ${newValue} Watts")	}
                    state.powerHigh = newValue
                    state.powerHighDisp = dispValue
                }
                state.powerValue = newValue
                [name: "power", value: newValue, unit: "W", descriptionText: "Total Power: ${newValue} Watts"]
            }
		}
 	}
}

def zwaveEvent(physicalgraph.zwave.commands.multichannelv3.MultiChannelCmdEncap cmd) {
	def dispValue
	def newValue
	def formattedValue
    def MAX_AMPS = 220
    def MAX_WATTS = 24000
                    
   	if (cmd.commandClass == 50) {
   		def encapsulatedCommand = cmd.encapsulatedCommand([0x30: 1, 0x31: 1]) // can specify command class versions here like in zwave.parse
		if (encapsulatedCommand) {
			if (cmd.sourceEndPoint == 1) {
				if (encapsulatedCommand.scale == 2 ) {
					newValue = Math.round(encapsulatedCommand.scaledMeterValue)
                    if (newValue > MAX_WATTS) { return }
					formattedValue = newValue as String
					dispValue = "${formattedValue}\nWatts"
					if (dispValue != state.powerL1Disp) {
						state.powerL1Disp = dispValue
						if (state.display == 2) {
							[name: "powerOne", value: dispValue, unit: "", descriptionText: "L1 Power: ${formattedValue} Watts"]
						}
					}
				} 
				else if (encapsulatedCommand.scale == 0 ){
					newValue = Math.round(encapsulatedCommand.scaledMeterValue * 100) / 100
            		state.energyOneInDevice = newValue;
            		newValue = newValue - state.energyOneInDeviceAtReset;
					formattedValue = String.format("%5.2f", newValue)
					dispValue = "${formattedValue}\nkWh"
					if (dispValue != state.energyL1Disp) {
						state.energyL1Disp = dispValue
						if (state.display == 2) {
							[name: "energyOne", value: dispValue, unit: "", descriptionText: "L1 Energy: ${formattedValue} kWh"]
						}
					}
				}
				else if (encapsulatedCommand.scale == 1 ){
					newValue = Math.round(encapsulatedCommand.scaledMeterValue * 100) / 100
					formattedValue = String.format("%5.2f", newValue)
					dispValue = "${formattedValue}\nkVAh"
					if (dispValue != state.energyL1Disp) {
						state.energyL1Disp = dispValue
						if (state.display == 2) {
							[name: "energyOne", value: dispValue, unit: "", descriptionText: "L1 Energy: ${formattedValue} kVAh"]
						}
					}
				}	
			} 
			else if (cmd.sourceEndPoint == 2) {
				if (encapsulatedCommand.scale == 2 ){
					newValue = Math.round(encapsulatedCommand.scaledMeterValue)
                    if (newValue > MAX_WATTS ) { return }
					formattedValue = newValue as String
					dispValue = "${formattedValue}\nWatts"
					if (dispValue != state.powerL2Disp) {
						state.powerL2Disp = dispValue
						if (state.display == 2) {
							[name: "powerTwo", value: dispValue, unit: "", descriptionText: "L2 Power: ${formattedValue} Watts"]
						}
					}
				} 
				else if (encapsulatedCommand.scale == 0 ){
					newValue = Math.round(encapsulatedCommand.scaledMeterValue * 100) / 100
                    state.energyTwoInDevice = newValue;
            		newValue = newValue - state.energyTwoInDeviceAtReset;
					formattedValue = String.format("%5.2f", newValue)
					dispValue = "${formattedValue}\nkWh"
					if (dispValue != state.energyL2Disp) {
						state.energyL2Disp = dispValue
						if (state.display == 2) {
							[name: "energyTwo", value: dispValue, unit: "", descriptionText: "L2 Energy: ${formattedValue} kWh"]
						}
					}
				} 
				else if (encapsulatedCommand.scale == 1 ){
					newValue = Math.round(encapsulatedCommand.scaledMeterValue * 100) / 100
					formattedValue = String.format("%5.2f", newValue)
					dispValue = "${formattedValue}\nkVAh"
					if (dispValue != state.energyL2Disp) {
						state.energyL2Disp = dispValue
						if (state.display == 2) {
							[name: "energyTwo", value: dispValue, unit: "", descriptionText: "L2 Energy: ${formattedValue} kVAh"]
						}
					}
				}
			}
		}
	}
}

def zwaveEvent(physicalgraph.zwave.commands.sensormultilevelv1.SensorMultilevelReport cmd) {
    def dispValue
    def newValue
    def MAX_WATTS = 24000
    
	def timeString = new Date().format("h:mm a", location.timeZone)
    
    if (cmd.sensorType == 4) { // Power
		if (cmd.scale == 0) {
        	newValue = Math.round(cmd.scaledSensorValue)		// really not worth the hassle to show decimals for Watts
            if (newValue > MAX_WATTS) { return }				// Ignore ridiculous values (a 200Amp supply @ 120volts is roughly 24000 watts)
        	if (newValue != state.powerValue) {
    			dispValue = newValue+"\nWatts"
                sendEvent(name: "powerDisp", value: dispValue as String, unit: "", descriptionText: "Display Power: ${newValue} Watts", displayed: false)
                
                if (newValue < state.powerLow) {
                	dispValue = newValue+"\n"+timeString
                	if (state.display == 1) { sendEvent(name: "powerOne", value: dispValue as String, unit: "", descriptionText: "Lowest Power: ${newValue} Watts")	}
                    state.powerLow = newValue
                    state.powerLowDisp = dispValue
                }
                if (newValue > state.powerHigh) {
                	dispValue = newValue+"\n"+timeString
                	if (state.display == 1) { sendEvent(name: "powerTwo", value: dispValue as String, unit: "", descriptionText: "Highest Power: ${newValue} Watts")	}
                    state.powerHigh = newValue
                    state.powerHighDisp = dispValue
                }
                state.powerValue = newValue
                [name: "power", value: newValue, unit: "W", descriptionText: "Total Power: ${newValue} Watts"]
            }
		}
	}
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
	// Handles all Z-Wave commands we aren't interested in
    logDebug("Unhandled event ${cmd}")
	[:]
}

def refresh() { // Request HEMv2 to send us the latest values for the 4 we are tracking
	logDebug "refresh()"
    
	delayBetween([
		zwave.meterV2.meterGet(scale: 0).format(),		// Change 0 to 1 if international version
		zwave.meterV2.meterGet(scale: 2).format(),
		zwave.meterV2.meterGet(scale: 4).format(),
		zwave.meterV2.meterGet(scale: 5).format()
	])
    resetDisplay()
}

def poll() {
	logDebug "poll()"
	refresh()
}

def toggleDisplay() {
	logDebug "toggleDisplay()"
    
	if (state.display == 1) { 
		state.display = 2 
	}
	else { 
		state.display = 1
	}
    
	resetDisplay()
}

def resetDisplay() {
	logDebug "resetDisplay() - energyL1Disp: ${state.energyL1Disp}"
	
	if ( state.display == 1 ) {
		sendEvent(name: "powerOne", value: state.powerLowDisp, unit: "")     
    	sendEvent(name: "energyOne", value: state.lastResetTime, unit: "")
    	sendEvent(name: "powerTwo", value: state.powerHighDisp, unit: "")
    	sendEvent(name: "energyTwo", value: state.costDisp, unit: "")    	
	}
	else { 
		sendEvent(name: "powerOne", value: state.powerL1Disp, unit: "")     
    	sendEvent(name: "energyOne", value: state.energyL1Disp, unit: "")	
    	sendEvent(name: "powerTwo", value: state.powerL2Disp, unit: "")
    	sendEvent(name: "energyTwo", value: state.energyL2Disp, unit: "")
	}
}

def reset() {
	logDebug "reset()"
    
	state.energyValue = -1
	state.powerValue = -1

    state.powerHigh = 0
    state.powerHighDisp = ""
    state.powerLow = 99999
    state.powerLowDisp = ""
 
    state.energyL1Disp = ""
    state.energyL2Disp = ""
    state.powerL1Disp = ""
    state.powerL2Disp = ""
    
    state.energyTotalInDeviceAtReset = state.energyTotalInDevice;
 	state.energyOneInDeviceAtReset = state.energyOneInDevice;
 	state.energyTwoInDeviceAtReset = state.energyTwoInDevice;
    
    if (!state.energyTotalInDeviceAtReset) { state.energyTotalInDeviceAtReset = 0; }
    if (!state.energyOneInDeviceAtReset) { state.energyOneInDeviceAtReset = 0; }
    if (!state.energyTwoInDeviceAtReset) { state.energyTwoInDeviceAtReset = 0; }
                    
    if (!state.display) { state.display = 1 }	// Sometimes it appears that installed() isn't called

    def dateString = new Date().format("M/d/YY", location.timeZone)
    def timeString = new Date().format("h:mm a", location.timeZone)    
	state.lastResetTime = "Since\n"+dateString+"\n"+timeString
	state.costDisp = "Cost\n--"

    resetDisplay()
    
    sendEvent(name: "energyDisp", value: "", unit: "")
    sendEvent(name: "powerDisp", value: "", unit: "")

// No V1 available
	def cmd = delayBetween( [
		zwave.meterV2.meterReset().format(),			// Reset all values
		zwave.meterV2.meterGet(scale: 0).format(),		// Request the values we are interested in (0-->1 for kVAh)
		zwave.meterV2.meterGet(scale: 2).format(),
		zwave.meterV2.meterGet(scale: 4).format(),
		zwave.meterV2.meterGet(scale: 5).format()
	], 1000)
    cmd
    
    configure()
}

def logDebug(String message) {
	if (settings.enableLog == 'true') {
    	log.debug message
	}
}

def logDebug(List<String> message) {
	if (settings.enableLog == 'true') {
    	log.debug message
	}
}

def configure() {
	logDebug "configure()"
    
	Long kDelay = settings.kWhDelay as Long
    Long dDelay = settings.detailDelay as Long
    Long voltage = settings.voltage as Long
    
    if (kDelay == null) {		// Shouldn't have to do this, but there seem to be initialization errors
		kDelay = 15
	}

	if (dDelay == null) {
		dDelay = 15
	}
    
    if (voltage == null) {
		voltage = 120
	}
    
	def cmd = delayBetween([
//  	zwave.configurationV1.configurationSet(parameterNumber: 255, size: 1, scaledConfigurationValue: 2).format(),      // Reset factory defaults
//		zwave.configurationV1.configurationSet(parameterNumber: 100, size: 1, scaledConfigurationValue: 0).format(),		// reset to defaults
		zwave.configurationV1.configurationSet(parameterNumber: 1, size: 2, scaledConfigurationValue: voltage).format(),
		zwave.configurationV1.configurationSet(parameterNumber: 3, size: 1, scaledConfigurationValue: 0).format(),			// Disable (=0) selective reporting
//		zwave.configurationV1.configurationSet(parameterNumber: 4, size: 2, scaledConfigurationValue: 5).format(),			// Don't send whole HEM unless watts have changed by 30
//		zwave.configurationV1.configurationSet(parameterNumber: 5, size: 2, scaledConfigurationValue: 5).format(),			// Don't send L1 Data unless watts have changed by 15
//		zwave.configurationV1.configurationSet(parameterNumber: 6, size: 2, scaledConfigurationValue: 5).format(),			// Don't send L2 Data unless watts have changed by 15
//      zwave.configurationV1.configurationSet(parameterNumber: 8, size: 1, scaledConfigurationValue: 1).format(),			// Or by 5% (whole HEM)
//		zwave.configurationV1.configurationSet(parameterNumber: 9, size: 1, scaledConfigurationValue: 1).format(),			// Or by 5% (L1)
//      zwave.configurationV1.configurationSet(parameterNumber: 10, size: 1, scaledConfigurationValue: 1).format(),			// Or by 5% (L2)
//		zwave.configurationV1.configurationSet(parameterNumber: 101, size: 4, scaledConfigurationValue: 6145).format(),   	// Whole HEM and L1/L2 power in kWh
//		zwave.configurationV1.configurationSet(parameterNumber: 111, size: 4, scaledConfigurationValue: kDelay).format(), 	// Default every 120 Seconds
//		zwave.configurationV1.configurationSet(parameterNumber: 102, size: 4, scaledConfigurationValue: 1573646).format(),  // L1/L2 for Amps & Watts, Whole HEM for Amps, Watts, & Volts
//		zwave.configurationV1.configurationSet(parameterNumber: 112, size: 4, scaledConfigurationValue: dDelay).format(), 	// Defaul every 30 seconds
		zwave.configurationV1.configurationSet(parameterNumber: 101, size: 4, scaledConfigurationValue: 6149).format(),   	// All L1/L2 kWh, total Volts & kWh
		zwave.configurationV1.configurationSet(parameterNumber: 111, size: 4, scaledConfigurationValue: 60).format(), 		// Every 60 seconds
		zwave.configurationV1.configurationSet(parameterNumber: 102, size: 4, scaledConfigurationValue: 1572872).format(),	// Amps L1, L2, Total
		zwave.configurationV1.configurationSet(parameterNumber: 112, size: 4, scaledConfigurationValue: 30).format(), 		// every 30 seconds
		zwave.configurationV1.configurationSet(parameterNumber: 103, size: 4, scaledConfigurationValue: 770).format(),		// Power (Watts) L1, L2, Total
		zwave.configurationV1.configurationSet(parameterNumber: 113, size: 4, scaledConfigurationValue: 6).format() 		// every 6 seconds
	], 2000)
	logDebug cmd

	cmd
}