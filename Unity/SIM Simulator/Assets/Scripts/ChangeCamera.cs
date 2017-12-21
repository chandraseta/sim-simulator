using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ChangeCamera : MonoBehaviour {
	
	public Camera frontCamera;
	public Camera backCamera;

	private void Awake ()
	{
		backCamera.enabled = false;
	}

	public void SwitchCam ()
	{
		frontCamera.enabled = !frontCamera.enabled;
		backCamera.enabled = !backCamera.enabled;
	}

	void Update() {
		if (Input.GetKeyDown (KeyCode.C)) {
			SwitchCam ();
		}
	}
}