using System.Collections;
using System.Collections.Generic;
using MarkLight;
using MarkLight.Views.UI;
using UnityEngine;
using UnityEngine.SceneManagement;

[HideInPresenter]
public class LevelSelectionView : UIView {

	public Button Stage1;
	public Button Stage2;
	public Button Stage3;
	public Button Stage4;
	public Button Stage5;
	public List<Button> StageButtons;
	public User CurrentUser;

	public void Start() {
		StageButtons = new List<Button> {
			Stage1, Stage2, Stage3, Stage4, Stage5
		};
		setButtonsStates(new bool[] {false, false, false, false, false});
		CurrentUser = User.Instance;
	}

	public void Update() {
		if (CurrentUser.TheoryScore >= 60) {
			bool b1 = true;
			bool b2 = CurrentUser.PracticalScore >= 1;
			bool b3 = CurrentUser.PracticalScore >= 2;
			bool b4 = CurrentUser.PracticalScore >= 3;
			bool b5 = CurrentUser.PracticalScore >= 4;
			
			setButtonsStates(new bool[]{b1, b2, b3, b4, b5});
		} else {
			setButtonsStates(new bool[]{false, false, false, false, false});
		}
	}

	public void PlayStage1() {
		Debug.Log("Stage 1");
		if (!Stage1.IsDisabled.Value)
			SceneManager.LoadScene("Stage1", LoadSceneMode.Single);
	}

	public void PlayStage2() {
		Debug.Log("Stage 2");
		if (!Stage2.IsDisabled.Value)
			SceneManager.LoadScene("Stage2", LoadSceneMode.Single);
	}

	public void PlayStage3() {
		Debug.Log("Stage 3");
		if (!Stage3.IsDisabled.Value)
			SceneManager.LoadScene("Stage3", LoadSceneMode.Single);
	}

	public void PlayStage4() {
		Debug.Log("Stage 4");
		if (!Stage4.IsDisabled.Value)
			SceneManager.LoadScene("Stage4", LoadSceneMode.Single);
	}

	public void PlayStage5() {
		Debug.Log("Stage 5");
		if (!Stage5.IsDisabled.Value)
			SceneManager.LoadScene("Stage5", LoadSceneMode.Single);
	}

	private void setButtonsStates(bool[] values) {
		string t = "Default";
		string f = "Disabled";

		for (int i = 0; i < 5; ++i) {
			StageButtons[i].SetState(values[i] ? t : f);
			StageButtons[i].IsDisabled.Value = !values[i];
		}
	}
}
