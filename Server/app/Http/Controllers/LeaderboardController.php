<?php

namespace App\Http\Controllers;

use App\Leaderboard;
use Illuminate\Http\Request;

class LeaderboardController extends Controller
{
    /**
     * Retrieves the entire Leaderboard table
     *
     * @return \Illuminate\Http\JsonResponse JSON response
     */
    public function index() {
        return json_encode(Leaderboard::all());
    }

    /**
     * Retrieves the Leaderboard table
     * Filters by question amount, sorted by correct amount
     *
     * @param $amount int Amount number to be retrieved
     * @return \Illuminate\Http\JsonResponse JSON response
     */
    public function indexByQuestionAmount($amount) {
        return json_encode(
            Leaderboard::where('questions_amount', $amount)
                ->orderBy('correct_amount', 'desc')
                ->get()
        );
    }

    /**
     * Creates a new leaderboard entry
     *
     * @param Request $request Entry data
     * @return \Illuminate\Http\JsonResponse Operation status
     */
    public function addEntry(Request $request) {
        $entry = Leaderboard::create($request->all());
        return response()->json($entry, 201);
    }

}
