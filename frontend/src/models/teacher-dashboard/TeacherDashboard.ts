import StudentStats from '@/models/teacher-dashboard/StudentStats';
import QuestionStats from '@/models/teacher-dashboard/QuestionStats';
import QuizStats from '@/models/teacher-dashboard/QuizStats';

export default class TeacherDashboard {
  id!: number;
  studentStats: StudentStats[] = [];
  questionStats: QuestionStats[] = [];
  quizStats: QuizStats[] = [];
  
  constructor(jsonObj?: TeacherDashboard) {
    if (jsonObj) {
      this.id = jsonObj.id;

      if (jsonObj.studentStats) {
        this.studentStats = jsonObj.studentStats.map(
          (studentStats: StudentStats) => new StudentStats(studentStats)
        );
      }

      if (jsonObj.questionStats) {
        this.questionStats = jsonObj.questionStats.map(
          (questionStats: QuestionStats) => new QuestionStats(questionStats)
        );
      }

      if (jsonObj.quizStats) {
        this.quizStats = jsonObj.quizStats.map(
          (quizStats: QuizStats) => new QuizStats(quizStats)
        );
      }

    }
  }
}
