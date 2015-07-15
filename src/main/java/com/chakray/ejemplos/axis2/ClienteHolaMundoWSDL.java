package com.chakray.ejemplos.axis2;

import java.rmi.RemoteException;

import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.rampart.RampartMessageData;

import com.chakray.ejemplos.axis2.HolamundoWSDLStub.Holaati;
import com.chakray.ejemplos.axis2.HolamundoWSDLStub.HolaatiResponse;
import com.chakray.ejemplos.axis2.HolamundoWSDLStub.Persona;

public class ClienteHolaMundoWSDL {

	private static Policy loadPolicy(String xmlPath) throws Exception {
		StAXOMBuilder builder = new StAXOMBuilder(xmlPath);
		return PolicyEngine.getPolicy(builder.getDocumentElement());
	}

	public static void main(String[] args) {

		try {
			// trusted key store
			String trustStore = null;
			trustStore = "wso2carbon.jks";

			// policy file
			String policyFilePath = "UTOverTransport.xml";

			// properties to encrypt the transport channel
			System.setProperty("javax.net.ssl.trustStore", trustStore);
			System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");

			// custom end point
			HolamundoWSDLStub stub = new HolamundoWSDLStub(
					"https://localhost:9445/services/HolamundoWSDL");

			// get the services options to engage the rampart module
			Options options = stub._getServiceClient().getOptions();
			stub._getServiceClient().engageModule("rampart");

			// setting user/pass
			options.setUserName("admin");
			options.setPassword("admin");

			// load policy file
			options.setProperty(RampartMessageData.KEY_RAMPART_POLICY,
					loadPolicy(policyFilePath));
			stub._getServiceClient().setOptions(options);

			Holaati holaati = new Holaati();
			Persona param = new Persona();
			param.setNombre("Jorge");
			param.setApellidos("Infante Osorio");
			holaati.setPersona(param);

			HolaatiResponse respuesta = stub.holaati(holaati);

			System.out.println(respuesta.get_return().getSaludo());

		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
