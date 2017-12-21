<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();
});

/*
 * Question API endpoint
 * No database changes are allowed, hence no available operation other than GET
 */
Route::get('questions', 'QuestionController@getRandom');
Route::get('questions/all', 'QuestionController@index');
Route::get('questions/id/{question}', 'QuestionController@show');
Route::get('questions/random/{size}', 'QuestionController@getRandom');
Route::get('questions/image/{id}', 'QuestionController@getImage');

/*
 * Leaderboard API endpoint
 */
Route::get('leaderboard', 'LeaderboardController@index');
Route::get('leaderboard/{amount}', 'LeaderboardController@indexByQuestionAmount');
Route::post('leaderboard/add', 'LeaderboardController@addEntry');

Route::post('users/register', 'UserController@register');
Route::post('users/auth', 'UserController@auth');
Route::get('users/get/{user}', 'UserController@get');
Route::post('users/post', 'UserController@post');

Route::any('foo', function () {

})->middleware('firebase.jwt');