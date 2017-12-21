<?php

use App\Leaderboard;
use Illuminate\Database\Seeder;

class LeaderboardsTableSeeder extends Seeder
{
    var $n = 50;
    var $qAmtSel = array(10, 30, 50, 100);

    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        Leaderboard::truncate();
        $faker = \Faker\Factory::create();

        for ($i = 0; $i < $this->n; $i++) {
            $qAmt = $this->qAmtSel[array_rand($this->qAmtSel)];

            Leaderboard::create([
                'name' => $faker->name(),
                'questions_amount' => $qAmt,
                'correct_amount' => rand(0, $qAmt),
                'time_taken' => rand(0, 86400)
            ]);
        }
    }
}
