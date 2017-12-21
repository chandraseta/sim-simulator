<?php

namespace App\Http\Controllers;

use App\Question;
use Illuminate\Database\QueryException;


use Intervention\Image\Facades\Image;


use Redeman\Imgur\Facades\Imgur;


class QuestionController extends Controller
{
    /**
     * Retrieves the entire Questions table
     *
     * @return \Illuminate\Http\JsonResponse The entire Questions table as JSON
     */
    public function index() {
        return json_encode(Question::all());
    }

    /**
     * Retrieves a specific Question with the specified ID
     *
     * @param Question $question ID of question to fetch
     * @return \Illuminate\Http\JsonResponse Question with specified ID as JSON
     */
    public function show(Question $question) {
        return json_encode($question);
    }

    /**
     * Retrieves a random set of questions.
     *
     * @param $size int Size of random set to retrieve.
     * @return \Illuminate\Http\JsonResponse JSON response
     */
    public function getRandom($size = 30) {
        $randomQuestion = null;
        try {
            $randomQuestion = Question::orderByRaw('RAND()')->take($size)->get();
        } catch (QueryException $qe) {
            $randomQuestion = Question::orderByRaw('random()')->take($size)->get();
        }

        return json_encode($randomQuestion);
    }

    /**
     * Retrieves image with specified ID.
     *
     * @param $id int ID of image
     * @return \Illuminate\Http\Response Image response
     */
    public function getImage($id) {
        $img = Imgur::api('image')->image($id);
        $path = $img->getLink();
        return Image::make($path)->response();
    }
}
