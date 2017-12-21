using System.Collections;
using System.Collections.Generic;
using MarkLight;
using MarkLight.Views.UI;
using UnityEngine;

[HideInPresenter]
public class OptionsView : UIView {

	[ChangeHandler("OptionsChanged")]
	public _bool TractionControl;

	[ChangeHandler("OptionsChanged")]
	public _bool ABS;

	[ChangeHandler("OptionsChanged")]
	public _bool ESP;

	public string ArduinoPort;

	public override void Initialize() {
		base.Initialize();
		TractionControl.Value = PlayerPrefs.HasKey("TC") ? PlayerPrefs.GetInt("TC") > 0 : true;
		ABS.Value = PlayerPrefs.HasKey("ABS") ? PlayerPrefs.GetInt("ABS") > 0 : true;
		ESP.Value = PlayerPrefs.HasKey("ESP") ? PlayerPrefs.GetInt("ESP") > 0 : true;
		ArduinoPort = PlayerPrefs.HasKey ("Port") ? PlayerPrefs.GetString ("Port") : "Arduino Port";
	}
	
	public void OptionsChanged() {
		Debug.Log("Options Changed");
		PlayerPrefs.SetInt("TC", TractionControl.Value ? 1 : 0);
		PlayerPrefs.SetInt("ABS", ABS.Value ? 1 : 0);
		PlayerPrefs.SetInt("ESP", ESP.Value ? 1 : 0);
		PlayerPrefs.SetString ("Port", ArduinoPort);
	}
}
