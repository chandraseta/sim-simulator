using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;

[Serializable]
public class UserObject {
    public string id;
	public string email;
	public string theory_score;
	public string practical_score;
	public string created_at;
	public string updated_at;
}

public sealed class User {
    private static readonly string AuthURL =
		"http://sim-simulator-server.herokuapp.com/api/users/auth";
	private static readonly string GetURL =
		"http://sim-simulator-server.herokuapp.com/api/users/get/";
	private static readonly string PostURL =
		"http://sim-simulator-server.herokuapp.com/api/users/post";

	private static readonly User instance = new User();
	public static User Instance {
        get { return instance; }
    }

	public int Id { get; private set; }
	public string Email;
	public string Password { private get; set; }
	public int TheoryScore { get; private set; }
	public int PracticalScore;

	public UnityWebRequest www { get; private set; }
	private UserObject CurrentUser = new UserObject();
	private bool running = false;

    // Explicit static constructor to tell C# compiler
    // not to mark type as beforefieldinit
    static User() { }

    private User() { }

	public IEnumerator Login() {
		while (running)
       		yield return new WaitForSeconds(0.1f);
		running = true;

		WWWForm form = new WWWForm();
		form.AddField("email", Email);
		form.AddField("password", Password);

		www = UnityWebRequest.Post(AuthURL, form);
        yield return www.SendWebRequest();
 
        if (www.isNetworkError || www.isHttpError) {
            Debug.Log(www.error);
        } else {
			if (www.responseCode == 200) {
            	Debug.Log("RESPONSE: " + www.downloadHandler.text);

				JsonUtility.FromJsonOverwrite(www.downloadHandler.text, CurrentUser);
				Id = Int32.Parse(CurrentUser.id);
				TheoryScore = Int32.Parse(CurrentUser.theory_score);
				PracticalScore = Int32.Parse(CurrentUser.practical_score);

				Debug.Log("Id: " + Id.ToString() +
					" | Theory Score: " + TheoryScore.ToString() +
					" | Practical Score: " + PracticalScore.ToString());
			} else {
				Debug.Log("Login failure");
			}
        }

		running = false;
	}

	public IEnumerator GetScore() {
		while (running)
       		yield return new WaitForSeconds(0.1f);
		running = true;

		string url = GetURL + Id.ToString();
		www = UnityWebRequest.Get(GetURL);
		yield return www.SendWebRequest();

		if (www.isNetworkError || www.isHttpError) {
            Debug.Log("ERROR: " + www.error);
        } else {
			if (www.responseCode == 200) {
            	Debug.Log(www.downloadHandler.text);

				JsonUtility.FromJsonOverwrite(www.downloadHandler.text, CurrentUser);
				TheoryScore = Int32.Parse(CurrentUser.theory_score);
				PracticalScore = Int32.Parse(CurrentUser.practical_score);

				Debug.Log("Id: " + Id.ToString() +
					" | Theory Score: " + TheoryScore.ToString() +
					" | Practical Score: " + PracticalScore.ToString());
			} else {
				string Message = "Error " + www.responseCode.ToString() +
					"\n" + www.downloadHandler.text;
				Debug.Log(Message);
			}
        }

		running = false;
	}

	public IEnumerator UpdateScore() {
		while (running)
       		yield return new WaitForSeconds(0.1f);
		running = true;
		
		WWWForm form = new WWWForm();
        form.AddField("id", Id);
		form.AddField("practical_score", PracticalScore);

		www = UnityWebRequest.Post(PostURL, form);
        yield return www.SendWebRequest();
 
        if (www.isNetworkError || www.isHttpError) {
            Debug.Log(www.error);
        } else {
			if (www.responseCode == 200) {
            	Debug.Log(www.downloadHandler.text);
			} else {
				Debug.Log("Update failure");
			}
        }

		running = false;
	}

	public void ResetUser() {
		Id = 0;
		Email = "";
		Password = "";
		TheoryScore = 0;
		PracticalScore = 0;
	}
}