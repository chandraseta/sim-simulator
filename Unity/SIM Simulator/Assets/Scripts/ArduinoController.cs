using System.Collections;
using System.Collections.Generic;
using System.IO.Ports;
using UnityEngine;

public class ArduinoController : MonoBehaviour {

	public string port = "COM6";
	public SerialPort stream;

	// Use this for initialization
	void Start () {
		if (PlayerPrefs.HasKey ("Port")) {
			port = PlayerPrefs.GetString ("Port");
		}
		stream = new SerialPort (port, 9600);
		stream.ReadTimeout = 50;
		stream.Open();
	}
	
	// Update is called once per frame
	void Update () {
		if (Input.GetKeyDown ("w")) {
			stream.WriteLine("F");
		}
		else if (Input.GetKeyDown("a")) {
			stream.WriteLine("L");
		}
		else if (Input.GetKeyDown("s")) {
			stream.WriteLine("B");
		}
		else if (Input.GetKeyDown("d")) {
			stream.WriteLine("R");
		}

		if (Input.GetKeyUp ("w") || Input.GetKeyUp ("a") || Input.GetKeyUp ("s") || Input.GetKeyUp ("d")) {
			stream.WriteLine("S");
		}
	}
}
