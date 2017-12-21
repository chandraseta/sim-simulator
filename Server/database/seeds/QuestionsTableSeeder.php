<?php

use App\Question;
use Illuminate\Database\Seeder;


class QuestionsTableSeeder extends Seeder
{
    var $n = 10;
    var $imgurImgId = 'aNSiN9g';

    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        Question::truncate();
        $faker = \Faker\Factory::create();

        for ($i = 0; $i < $this->n; $i++) {
            Question::create([
                'body' => $faker->sentences($nb = 3, $asText = true),
                'image_id' => rand(0, 1) ? null : $this->imgurImgId,
                'answer' => $faker->sentence,
                'answer_alt_1' => $faker->sentence,
                'answer_alt_2' => $faker->sentence,
                'answer_alt_3' => $faker->sentence
            ]);
        }
    }
}
