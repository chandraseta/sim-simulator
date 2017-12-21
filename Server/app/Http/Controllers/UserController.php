<?php

namespace App\Http\Controllers;

use App\User;
use Illuminate\Support\Facades\DB;
use Exception;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Psr\Log\InvalidArgumentException;
use Session;
use Symfony\Component\Debug\Exception\FatalThrowableError;

class UserController extends Controller
{
    public function auth(Request $request) {
        $email = $request->email;
        $password = $request->password;

        if (Auth::attempt(['email' => $email, 'password' => $password])) {
            $user = DB::table('users')
                ->where('email', $email)
                ->first();
            return response(User::find($user->id), 200)
                ->header('Content-Type', 'text/plain');
        } else {
            return response(-1, 403);
        }
    }

    public function register(Request $request) {
        $name = $request->name;
        $email = $request->email;
        $password = $request->password;

        return User::create([
            'name' => $name,
            'email' => $email,
            'password' => bcrypt($password)
        ]);
    }

    public function get(User $user) {
        return json_encode($user);
    }

    public function post(Request $request) {
        $id = $request->id;
        $email = $request->email;

        if ($id != null) {
            $user = User::find($id);
            $found = ($user != null);
        } else if ($email != null) {
            $user = DB::table('users')
                ->where('email', $email)
                ->first();
            $found = ($user->id != null);
            $id = $user->id;
        } else {
            $found = false;
        }

        if (!$found) {
            throw new InvalidArgumentException("ID or Email not found");
        }

        $old_theory_score = $user->theory_score;
        $old_practical_score = $user->practical_score;

        $update = array();
        if ($theory_score = $request->theory_score) {
            if ($theory_score > $old_theory_score) {
                $update['theory_score'] = $theory_score;
            }
        }
        if ($practical_score = $request->practical_score) {
            if ($practical_score > $old_practical_score) {
                $update['practical_score'] = $practical_score;
            }
        }

        if (!empty($update)) {
            $entry = DB::table('users')
                ->where('id', $id)
                ->update($update);
            return response()->json($entry, 201);
        }
        return response()->json(0, 201);
    }
}
