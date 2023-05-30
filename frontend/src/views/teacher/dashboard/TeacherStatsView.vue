<template>
  <div class="container">
    <h2>Statistics for this course execution</h2>
    <div v-if="teacherDashboard != null" class="stats-container">
      <div class="items">
        <div ref="numStudents" class="icon-wrapper">
          <animated-number
            :number="teacherDashboard.studentStats[0].numStudents"
          />
        </div>
        <div class="project-name">
          <p>Number of Students</p>
        </div>
      </div>
      <div class="items">
        <div ref="studentsMore75CorrectQuestions" class="icon-wrapper">
          <animated-number
            :number="teacherDashboard.studentStats[0].numMore75CorrectQuestions"
          />
        </div>
        <div class="project-name">
          <p>Number of Students who Solved >= 75% Questions</p>
        </div>
      </div>
      <div class="items">
        <div ref="studentsAtLeast3Quizzes" class="icon-wrapper">
          <animated-number
            :number="teacherDashboard.studentStats[0].numAtLeast3Quizzes"
          />
        </div>
        <div class="project-name">
          <p>Number of Students who Solved >= 3 Quizzes</p>
        </div>
      </div>
      <div class="items">
        <div ref="totalQuizzes" class="icon-wrapper">
          <animated-number :number="teacherDashboard.quizStats[0].numQuizzes" />
        </div>
        <div class="project-name">
          <p>Number of Quizzes</p>
        </div>
      </div>
      <div class="items">
        <div ref="uniqueQuizzesSolved" class="icon-wrapper">
          <animated-number
            :number="teacherDashboard.quizStats[0].numUniqueAnsweredQuizzes"
          />
        </div>
        <div class="project-name">
          <p>Number of Quizzes Solved (Unique)</p>
        </div>
      </div>
      <div class="items">
        <div ref="averageUniqueQuizzesSolved" class="icon-wrapper">
          <animated-number
            :number="teacherDashboard.quizStats[0].averageQuizzesSolved"
          />
        </div>
        <div class="project-name">
          <p>Number of Quizzes Solved (Unique, Average Per Student)</p>
        </div>
      </div>
      <div class="items">
        <div ref="numAvailable" class="icon-wrapper">
          <animated-number
            :number="teacherDashboard.questionStats[0].numAvailable"
          />
        </div>
        <div class="project-name">
          <p>Number of Questions</p>
        </div>
      </div>
      <div class="items">
        <div ref="answeredQuestionsUnique" class="icon-wrapper">
          <animated-number
            :number="teacherDashboard.questionStats[0].answeredQuestionsUnique"
          />
        </div>
        <div class="project-name">
          <p>Number of Questions Solved (Unique)</p>
        </div>
      </div>
      <div class="items">
        <div ref="averageQuestionsAnswered" class="icon-wrapper">
          <animated-number
            :number="teacherDashboard.questionStats[0].averageQuestionsAnswered"
          />
        </div>
        <div class="project-name">
          <p>
            Number of Questions Correctly Solved (Unique, Average Per Student)
          </p>
        </div>
      </div>
    </div>
    <h2>Comparison with previous course executions</h2>
    <div v-if="teacherDashboard != null" class="stats-container">
      <div class="bar-chart">
        <Bar :chartData="studentStats()" :options="chartOptions()" />
      </div>
      <div class="bar-chart">
        <Bar :chartData="quizStats()" :options="chartOptions()" />
      </div>
      <div class="bar-chart">
        <Bar :chartData="questionStats()" :options="chartOptions()" />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  import { Component, Prop, Vue } from 'vue-property-decorator';
  import RemoteServices from '@/services/RemoteServices';
  import AnimatedNumber from '@/components/AnimatedNumber.vue';
  import TeacherDashboard from '@/models/teacher-dashboard/TeacherDashboard';

  import {
    Chart as ChartJS,
    Title,
    Tooltip,
    Legend,
    BarElement,
    CategoryScale,
    LinearScale,
  } from 'chart.js';
  
  import { Bar } from 'vue-chartjs/legacy';

  ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
  );

  @Component({
    components: { 
      AnimatedNumber,
      Bar
    },
  })

export default class TeacherStatsView extends Vue {
  @Prop() readonly dashboardId!: number;
  teacherDashboard: TeacherDashboard | null = null;

  // Colors for the charts' bars
  firstBarColor = '#bc3d35';
  secondBarColor = '#3687b4';
  thirdBarColor = '#34bc9c';

  async created() {
    await this.$store.dispatch('loading');
    try {
      this.teacherDashboard =
        await RemoteServices.getTeacherDashboard()
    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  chartOptions() {
    return {
      responsive: true,
      maintainAspectRatio: false,
    };
  }

  studentStats() {
    return {
      labels: [
        this.teacherDashboard?.studentStats[2]
          ? this.teacherDashboard?.studentStats[2].courseExecutionYear
          : '',
        this.teacherDashboard?.studentStats[1]
          ? this.teacherDashboard?.studentStats[1].courseExecutionYear
          : '',
        this.teacherDashboard?.studentStats[0].courseExecutionYear + ' (current)',
      ],
      datasets: [
        {
          label: 'Total Number of Students',
          backgroundColor: this.firstBarColor,
          data: [
            this.teacherDashboard?.studentStats[2]
              ? this.teacherDashboard?.studentStats[2].numStudents
              : 0,
            this.teacherDashboard?.studentStats[1].numStudents
              ? this.teacherDashboard?.studentStats[1].numStudents
              : 0,
            this.teacherDashboard?.studentStats[0].numStudents,
          ],
        },
        {
          label: 'Students who Solved >= 75% Questions',
          backgroundColor: this.secondBarColor,
          data: [
            this.teacherDashboard?.studentStats[2]
              ? this.teacherDashboard?.studentStats[2].numMore75CorrectQuestions
              : 0,
            this.teacherDashboard?.studentStats[1]
              ? this.teacherDashboard?.studentStats[1].numMore75CorrectQuestions
              : 0,
            this.teacherDashboard?.studentStats[0].numMore75CorrectQuestions,
          ],
        },
        {
          label: 'Students who Solved >= 3 Quizzes',
          backgroundColor: this.thirdBarColor,
          data: [
            this.teacherDashboard?.studentStats[2]
              ? this.teacherDashboard?.studentStats[2].numAtLeast3Quizzes
              : 0,
            this.teacherDashboard?.studentStats[1]
              ? this.teacherDashboard?.studentStats[1].numAtLeast3Quizzes
              : 0,
            this.teacherDashboard?.studentStats[0].numAtLeast3Quizzes,
          ],
        },
      ],
    };
  }

  questionStats() {
    return {
      labels: [
        this.teacherDashboard?.questionStats[2]
          ? this.teacherDashboard?.questionStats[2].courseExecutionYear
          : '',
        this.teacherDashboard?.questionStats[1]
          ? this.teacherDashboard?.questionStats[1].courseExecutionYear
          : '',
        this.teacherDashboard?.questionStats[0].courseExecutionYear + ' (current)',
      ],
      datasets: [
        {
          label: 'Questions: Total Available',
          backgroundColor: this.firstBarColor,
          data: [
            this.teacherDashboard?.questionStats[2]
              ? this.teacherDashboard?.questionStats[2].numAvailable
              : 0,
            this.teacherDashboard?.questionStats[1]
              ? this.teacherDashboard?.questionStats[1].numAvailable
              : 0,
            this.teacherDashboard?.questionStats[0].numAvailable,
          ]
        },
        {
          label: 'Questions: Total Solved (Unique) ',
          backgroundColor: this.secondBarColor,
          data: [
            this.teacherDashboard?.questionStats[2]
              ? this.teacherDashboard?.questionStats[2].answeredQuestionsUnique
              : 0,
            this.teacherDashboard?.questionStats[1]
              ? this.teacherDashboard?.questionStats[1].answeredQuestionsUnique
              : 0,
            this.teacherDashboard?.questionStats[0].answeredQuestionsUnique,
          ]
        },
        {
          label: 'Questions: Correctly Solved (Unique, Average Per Student)',
          backgroundColor: this.thirdBarColor,
          data: [
            this.teacherDashboard?.questionStats[2]
              ? this.teacherDashboard?.questionStats[2].averageQuestionsAnswered
              : 0,
            this.teacherDashboard?.questionStats[1]
              ? this.teacherDashboard?.questionStats[1].averageQuestionsAnswered
              : 0,
            this.teacherDashboard?.questionStats[0].averageQuestionsAnswered,
          ]
        },
      ],
    };
  }

  quizStats() {
    return {
      labels: [
        this.teacherDashboard?.quizStats[2]
          ? this.teacherDashboard?.quizStats[2].courseExecutionYear
          : '',
        this.teacherDashboard?.quizStats[1]
          ? this.teacherDashboard?.quizStats[1].courseExecutionYear
          : '',
        this.teacherDashboard?.quizStats[0].courseExecutionYear + ' (current)',
      ],
      datasets: [
        {
          label: 'Quizzes: Total Available',
          backgroundColor: this.firstBarColor,
          data: [
            this.teacherDashboard?.quizStats[2]
              ? this.teacherDashboard?.quizStats[2].numQuizzes
              : 0,
            this.teacherDashboard?.quizStats[1]
              ? this.teacherDashboard?.quizStats[1].numQuizzes
              : 0,
            this.teacherDashboard?.quizStats[0].numQuizzes,
          ]
        },
        {
          label: 'Quizzes: Solved (Unique)',
          backgroundColor: this.secondBarColor,
          data: [
            this.teacherDashboard?.quizStats[2]
              ? this.teacherDashboard?.quizStats[2].averageQuizzesSolved
              : 0,
            this.teacherDashboard?.quizStats[1]
              ? this.teacherDashboard?.quizStats[1].averageQuizzesSolved
              : 0,
            this.teacherDashboard?.quizStats[0].averageQuizzesSolved,
          ]
        },
        {
          label: 'Quizzes: Solved (Unique, Average Per Student)',
          backgroundColor: this.thirdBarColor,
          data: [
            this.teacherDashboard?.quizStats[2]
              ? this.teacherDashboard?.quizStats[2].numUniqueAnsweredQuizzes
              : 0,
            this.teacherDashboard?.quizStats[1]
              ? this.teacherDashboard?.quizStats[1].numUniqueAnsweredQuizzes
              : 0,
            this.teacherDashboard?.quizStats[0].numUniqueAnsweredQuizzes,
          ]
        },
      ],
    };
  }

}

</script>

<style lang="scss" scoped>
.stats-container {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: center;
  align-items: stretch;
  align-content: center;
  height: 100%;

  .items {
    background-color: rgba(255, 255, 255, 0.75);
    color: #1976d2;
    border-radius: 5px;
    flex-basis: 25%;
    margin: 20px;
    cursor: pointer;
    transition: all 0.6s;
  }

  .bar-chart {
    display: flex;
    background-color: rgba(255, 255, 255, 0.9);
    height: 400px;
    margin: 20px;
  }

}

.icon-wrapper,
.project-name {
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon-wrapper {
  font-size: 100px;
  transform: translateY(0px);
  transition: all 0.6s;
}

.icon-wrapper {
  align-self: end;
}

.project-name {
  align-self: start;
}

.project-name p {
  font-size: 24px;
  font-weight: bold;
  letter-spacing: 2px;
  transform: translateY(0px);
  transition: all 0.5s;
}

.items:hover {
  border: 3px solid black;

  & .project-name p {
    transform: translateY(-10px);
  }

  & .icon-wrapper i {
    transform: translateY(5px);
  }
}
</style>
