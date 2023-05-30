package pt.ulisboa.tecnico.socialsoftware.tutor.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswerItem;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.repository.QuestionAnswerItemRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.repository.QuizAnswerRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.studentdashboard.domain.DifficultQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.studentdashboard.repository.StudentDashboardRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.QuestionStatsRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.StudentStatsRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.TeacherDashboardRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.studentdashboard.repository.DifficultQuestionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.discussion.domain.Discussion;
import pt.ulisboa.tecnico.socialsoftware.tutor.discussion.repository.DiscussionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.CourseExecutionService;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.Assessment;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.TopicConjunction;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.repository.AssessmentRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.repository.CourseExecutionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.QuestionService;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.QuestionDetails;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.CourseRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.OptionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.TopicRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.questionsubmission.repository.QuestionSubmissionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.repository.QuizQuestionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.repository.QuizRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.repository.TournamentRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.repository.UserRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.dto.AuthDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User;
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.AuthUserService;
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.repository.QuestionAnswerRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.repository.AnswerDetailsRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.QuizStatsRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
public class DemoService {
    List<Integer> questions2Keep = Arrays.asList(1320, 1940, 1544, 11081, 11082);

    List<Integer> questionsInQuizzes = Arrays.asList(1940, 11081, 11082);

    Integer quiz2Keep = 40438;

    List<String> academicTerms = Arrays.asList("1st semester 2019/2020", "1st semester 2022/2023", "1st semester 2023/2024");

    @Autowired
    private CourseExecutionRepository courseExecutionRepository;

    @Autowired
    private CourseExecutionService courseExecutionService;

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionAnswerItemRepository questionAnswerItemRepository;

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private QuestionSubmissionRepository questionSubmissionRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizAnswerRepository quizAnswerRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentDashboardRepository studentDashboardRepository;

    @Autowired
    private DifficultQuestionRepository difficultQuestionRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private QuestionAnswerRepository questionAnswerRepository;

    @Autowired
    private AnswerDetailsRepository answerDetailsRepository;

    @Autowired
    private TeacherDashboardRepository teacherDashboardRepository;

    @Autowired
    private StudentStatsRepository studentStatsRepository;

    @Autowired
    private QuestionStatsRepository questionStatsRepository;

    @Autowired
    private QuizStatsRepository quizStatsRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void resetDemoDashboards() {
        userRepository.findAll()
                .stream()
                .filter(user -> user.getAuthUser() != null && user.getAuthUser().isDemoStudent())
                .map(Student.class::cast)
                .flatMap(student -> student.getDashboards().stream())
                .forEach(dashboard -> {
                    dashboard.remove();
                    this.studentDashboardRepository.delete(dashboard);
                });

        Set<DifficultQuestion> difficultQuestionsToRemove = courseExecutionRepository.findById(courseExecutionService.getDemoCourse().getCourseExecutionId()).stream()
                .flatMap(courseExecution -> courseExecution.getDifficultQuestions().stream())
                .collect(Collectors.toSet());

        difficultQuestionsToRemove.forEach(difficultQuestion -> {
            difficultQuestion.remove();
            difficultQuestionRepository.delete(difficultQuestion);
        });
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void resetDemoAssessments() {
        Integer courseExecutionId = courseExecutionService.getDemoCourse().getCourseExecutionId();

        this.assessmentRepository.findByExecutionCourseId(courseExecutionId)
                .stream()
                .forEach(assessment -> {
                    assessment.remove();
                    assessmentRepository.delete(assessment);
                });
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void resetDemoTopics() {
        Integer courseId = courseExecutionService.getDemoCourse().getCourseId();

        this.topicRepository.findTopics(courseId)
                .stream()
                .forEach(topic -> {
                    topic.remove();
                    this.topicRepository.delete(topic);
                });
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void resetDemoDiscussions() {
        List<Discussion> discussions = discussionRepository.findByExecutionCourseId(courseExecutionService.getDemoCourse().getCourseExecutionId());

        discussions.forEach(discussion -> {
            discussion.remove();
            discussionRepository.delete(discussion);
        });
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void resetDemoAnswers() {
        Set<QuestionAnswerItem> questionAnswerItems = questionAnswerItemRepository.findDemoStudentQuestionAnswerItems();
        questionAnswerItemRepository.deleteAll(questionAnswerItems);

        Set<QuizAnswer> quizAnswers = quizAnswerRepository.findByExecutionCourseId(courseExecutionService.getDemoCourse().getCourseExecutionId());

        for (QuizAnswer quizAnswer : quizAnswers) {
            if (!quizAnswer.getQuiz().getId().equals(quiz2Keep) || !quizAnswer.getStudent().getUsername().equals(DemoUtils.STUDENT_USERNAME)) {
                quizAnswer.remove();
                quizAnswerRepository.delete(quizAnswer);
            }
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void resetDemoTournaments() {
        tournamentRepository.getTournamentsForCourseExecution(courseExecutionService.getDemoCourse().getCourseExecutionId())
                .forEach(tournament -> {
                    tournament.getParticipants().forEach(user -> user.removeTournament(tournament));
                    if (tournament.getQuiz() != null) {
                        tournament.getQuiz().setTournament(null);
                    }

                    tournamentRepository.delete(tournament);
                });
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void resetDemoQuizzes() {
        // remove quizzes except to keep
        quizRepository.findQuizzesOfExecution(courseExecutionService.getDemoCourse().getCourseExecutionId())
                .stream()
                .forEach(quiz -> {
                    if (!quiz.getId().equals(quiz2Keep)) {

                        quiz.remove();
                        this.quizRepository.delete(quiz);
                    }
                });

        // remove questions except to keep and that are not submitted
        for (Question question : questionRepository.findQuestions(courseExecutionService.getDemoCourse().getCourseId())
                .stream()
                .filter(question -> !questions2Keep.contains(question.getId()) && questionSubmissionRepository.findQuestionSubmissionByQuestionId(question.getId()) == null)
                .collect(Collectors.toList())) {

            questionService.removeQuestion(question.getId());
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void resetDemoQuestionSubmissions() {
        questionSubmissionRepository.findQuestionSubmissionsByCourseExecution(courseExecutionService.getDemoCourse().getCourseExecutionId())
                .forEach(questionSubmission -> {
                    questionSubmission.remove();
                    questionSubmissionRepository.delete(questionSubmission);
                });
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void resetDemoStudents() {
        userRepository.findAll()
                .stream()
                .filter(user -> user.getAuthUser() != null && user.getAuthUser().isDemoStudent())
                .map(Student.class::cast)
                .forEach(student -> {
                    if (student.getQuizAnswers().isEmpty()) {
                        student.remove();
                        this.userRepository.delete(student);
                    }
                });
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)    
    public void resetDemoCourseQuizzes() {
        quizRepository.findAll()
                .stream()
                .filter(quiz -> quiz.getCourseExecution().getAcademicTerm().equals(academicTerms.get(0)) ||
                        quiz.getCourseExecution().getAcademicTerm().equals(academicTerms.get(1)) ||
                        quiz.getCourseExecution().getAcademicTerm().equals(academicTerms.get(2)))
                .forEach(quiz -> {
                    quiz.remove();
                    quizRepository.delete(quiz);
                });
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void resetDemoQuizAnswers() {
        quizAnswerRepository.findAll()
                .stream()
                .filter(quizAnswer -> quizAnswer.getQuiz().getCourseExecution().getAcademicTerm().equals(academicTerms.get(0)) ||
                        quizAnswer.getQuiz().getCourseExecution().getAcademicTerm().equals(academicTerms.get(1)) ||
                        quizAnswer.getQuiz().getCourseExecution().getAcademicTerm().equals(academicTerms.get(2)))
                .forEach(quizAnswer -> {
                    quizAnswer.remove();
                    quizAnswerRepository.delete(quizAnswer);
                });
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void resetDemoQuestionAnswers() {
        Integer demoCourseId = courseExecutionService.getDemoCourse().getCourseId();

        questionAnswerRepository.findAll()
                .stream()
                .filter(questionAnswer -> questionAnswer.getQuestion().getCourse().getId().equals(demoCourseId))
                .forEach(questionAnswer -> {
                    questionAnswer.remove();
                    questionAnswerRepository.delete(questionAnswer);
                });
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void resetDemoDetails() {
        Integer demoCourseId = courseExecutionService.getDemoCourse().getCourseId();

        answerDetailsRepository.findAll()
                .stream()
                .filter(answerDetails -> answerDetails.getQuestionAnswer().getQuestion().getCourse().getId().equals(demoCourseId))
                .forEach(questionDetails -> {
                    questionDetails.remove();
                    answerDetailsRepository.delete(questionDetails);
                });
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void resetDemoQuizQuestions() {
        Integer demoCourseId = courseExecutionService.getDemoCourse().getCourseId();

        quizQuestionRepository.findAll()
                .stream()
                .filter(quizQuestion -> quizQuestion.getQuestion().getCourse().getId().equals(demoCourseId))
                .forEach(quizQuestion -> {
                    quizQuestion.remove();
                    quizQuestionRepository.delete(quizQuestion);
                });
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void resetDemoExecutions() {
        Integer demoCourseId = courseExecutionService.getDemoCourse().getCourseId();

        studentStatsRepository.findAll()
                .stream()
                .filter(studentStats -> studentStats.getCourseExecution().getCourse().getId().equals(demoCourseId))
                .forEach(studentStats -> {
                    studentStats.remove();
                    studentStatsRepository.delete(studentStats);
                });

        questionStatsRepository.findAll()
                .stream()
                .filter(questionStats -> questionStats.getCourseExecution().getCourse().getId().equals(demoCourseId))
                .forEach(questionStats -> {
                    questionStats.remove();
                    questionStatsRepository.delete(questionStats);
                });

        quizStatsRepository.findAll()
                .stream()
                .filter(quizStats -> quizStats.getCourseExecution().getCourse().getId().equals(demoCourseId))
                .forEach(quizStats -> {
                    quizStats.remove();
                    quizStatsRepository.delete(quizStats);
                });

        teacherDashboardRepository.findAll()
                .stream()
                .filter(teacherDashboard -> teacherDashboard.getCourseExecution().getCourse().getId().equals(demoCourseId))
                .forEach(teacherDashboard -> {
                    teacherDashboard.remove();
                    teacherDashboardRepository.delete(teacherDashboard);
                });

        courseExecutionRepository.findAll().stream()
                .filter(courseExec -> courseExec.getCourse().getId().equals(demoCourseId))
                .forEach(courseExec -> {
                    courseExec.remove();
                    courseExecutionRepository.delete(courseExec);
                    }
                );
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    private void createExecutions() {
        Integer courseId = courseExecutionService.getDemoCourse().getCourseId();
        Integer courseExecutionId = courseExecutionService.getDemoCourse().getCourseExecutionId();

        // Let's update the end date of the demo execution
        CourseExecution demoExecution = courseExecutionRepository.findById(courseExecutionId)
                .orElseThrow(() -> new TutorException(ErrorMessage.COURSE_EXECUTION_NOT_FOUND));
        demoExecution.setEndDate(DateHandler.toLocalDateTime("2017-12-31T10:15:30+01:00[Europe/Lisbon]"));

        // Simulate login of demo teacher (this adds the demo teacher to the original demo execution).
        AuthDto authDemoTeacherDto = authUserService.demoTeacherAuth();

        // Get demo course and demo teacher
        Course demoCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new TutorException(ErrorMessage.COURSE_NOT_FOUND));
        User demoTeacher = userRepository.findById(authDemoTeacherDto.getUser().getId())
                .orElseThrow(() -> new TutorException(ErrorMessage.USER_NOT_FOUND));

        // End dates and academic terms used for the multiple tests for the dashboard statistics.
        List<String> endDates = Arrays.asList(
            "2019-12-31T10:15:30+01:00[Europe/Lisbon]",
            "2022-12-31T10:15:30+01:00[Europe/Lisbon]",
            "2023-12-31T10:15:30+01:00[Europe/Lisbon]"
        );

        for (int i = 0; i < endDates.size(); i++) {
            CourseExecution newCE = new CourseExecution(
                    demoCourse,
                    "Demo Course",
                    academicTerms.get(i),
                    Course.Type.TECNICO,
                    DateHandler.toLocalDateTime(endDates.get(i)));

            demoTeacher.addCourse(newCE);

            courseExecutionRepository.save(newCE);

            // Fill the new course execution with the student statistics.
            populateStats(newCE, i);
        }

    }

    private void populateStats(CourseExecution courseExecution, int i) {

        if (courseExecution.getAcademicTerm().equals(academicTerms.get(0))) {
            // 5 students
            Student student1 = createStudent("User 4 Name", courseExecution);
            Student student2 = createStudent("User 5 Name", courseExecution);
            Student student3 = createStudent("User 6 Name", courseExecution);
            Student student4 = createStudent("User 7 Name", courseExecution);
            Student student5 = createStudent("User 8 Name", courseExecution);

            Quiz quiz = createQuiz("Quiz 1 Title", courseExecution);

            // 3 students submit the quiz with 4/4, 3/4 and 1/4 correct answers.
            QuizQuestion quizQuestion1 = createQuizQuestion(quiz, 0, "Question 6 Title", "Question 6 Content", courseExecution, "Question 6 Option 1", "Question 6 Option 2", Question.Status.SUBMITTED);
            QuizQuestion quizQuestion2 = createQuizQuestion(quiz, 1, "Question 7 Title", "Question 7 Content", courseExecution, "Question 7 Option 1", "Question 7 Option 2", Question.Status.SUBMITTED);
            QuizQuestion quizQuestion3 = createQuizQuestion(quiz, 2, "Question 8 Title", "Question 8 Content", courseExecution, "Question 8 Option 1", "Question 8 Option 2", Question.Status.SUBMITTED);
            QuizQuestion quizQuestion4 = createQuizQuestion(quiz, 3, "Question 9 Title", "Question 9 Content", courseExecution, "Question 9 Option 1", "Question 9 Option 2", Question.Status.SUBMITTED);
            
            QuizAnswer quizAnswer1 = createQuizAnswer(quiz, student1, true);
            createQuestionAnswer(quizAnswer1, quizQuestion1, true);
            createQuestionAnswer(quizAnswer1, quizQuestion2, true);
            createQuestionAnswer(quizAnswer1, quizQuestion3, true);
            createQuestionAnswer(quizAnswer1, quizQuestion4, true);

            QuizAnswer quizAnswer2 = createQuizAnswer(quiz, student2, true);
            createQuestionAnswer(quizAnswer2, quizQuestion1, true);
            createQuestionAnswer(quizAnswer2, quizQuestion2, true);
            createQuestionAnswer(quizAnswer2, quizQuestion3, true);
            createQuestionAnswer(quizAnswer2, quizQuestion4, false);

            QuizAnswer quizAnswer3 = createQuizAnswer(quiz, student3, true);
            createQuestionAnswer(quizAnswer3, quizQuestion1, true);
            createQuestionAnswer(quizAnswer3, quizQuestion2, false);
            createQuestionAnswer(quizAnswer3, quizQuestion3, false);
            createQuestionAnswer(quizAnswer3, quizQuestion4, false);

            // 1 student starts the quiz but does not submit it.
            QuizAnswer quizAnswer4 = createQuizAnswer(quiz, student4, false);
            createQuestionAnswer(quizAnswer4, quizQuestion1, true);
            createQuestionAnswer(quizAnswer4, quizQuestion2, true);
            createQuestionAnswer(quizAnswer4, quizQuestion3, true);
            createQuestionAnswer(quizAnswer4, quizQuestion4, true);

            // 1 student answers only 1 question.
            QuizAnswer quizAnswer5 = createQuizAnswer(quiz, student5, false);
            createQuestionAnswer(quizAnswer5, quizQuestion1, true);

            /*
             * The final statistics for this execution should be:
             * ESA:
             *  - numStudents = 5; numMore75CorrectQuestions = 1; numAtLeast3Quizzes = 0;
             * ESQ:
             *  - numQuizzes = 1; numUniqueAnsweredQuizzes = 0.6; averageQuizzesSolved = 1;
             * ESP:
             *  - numAvailable = 0; answeredQuestionsUnique = 4; averageQuestionsAnswered = 3.4;
             */
        }
        else if (courseExecution.getAcademicTerm().equals(academicTerms.get(1))) {
            // 3 students.
            Student student1 = createStudent("User 9 Name", courseExecution);
            Student student2 = createStudent("User 10 Name", courseExecution);
            Student student3 = createStudent("User 11 Name", courseExecution);

            // 3 quizzes.
            Quiz quiz1 = createQuiz("Quiz 2 Title", courseExecution);
            Quiz quiz2 = createQuiz("Quiz 3 Title", courseExecution);
            Quiz quiz3 = createQuiz("Quiz 4 Title", courseExecution);

            // 2 questions for each quiz.
            QuizQuestion quizQuestion1 = createQuizQuestion(quiz1, 0, "Question 10 Title", "Question 10 Content", courseExecution, "Question 10 Option 1", "Question 10 Option 2", Question.Status.SUBMITTED);
            QuizQuestion quizQuestion2 = createQuizQuestion(quiz1, 1, "Question 11 Title", "Question 11 Content", courseExecution, "Question 11 Option 1", "Question 11 Option 2", Question.Status.SUBMITTED);
            
            QuizQuestion quizQuestion3 = createQuizQuestion(quiz2, 0, "Question 12 Title", "Question 12 Content", courseExecution, "Question 12 Option 1", "Question 12 Option 2", Question.Status.SUBMITTED);
            QuizQuestion quizQuestion4 = createQuizQuestion(quiz2, 1, "Question 13 Title", "Question 13 Content", courseExecution, "Question 13 Option 1", "Question 13 Option 2", Question.Status.SUBMITTED);

            QuizQuestion quizQuestion5 = createQuizQuestion(quiz3, 0, "Question 14 Title", "Question 14 Content", courseExecution, "Question 14 Option 1", "Question 14 Option 2", Question.Status.SUBMITTED);
            QuizQuestion quizQuestion6 = createQuizQuestion(quiz3, 1, "Question 15 Title", "Question 15 Content", courseExecution, "Question 15 Option 1", "Question 15 Option 2", Question.Status.SUBMITTED);
            
            // 1 student answers all quizzes correctly.
            QuizAnswer quizAnswer1 = createQuizAnswer(quiz1, student1, true);
            createQuestionAnswer(quizAnswer1, quizQuestion1, true);
            createQuestionAnswer(quizAnswer1, quizQuestion2, true);

            QuizAnswer quizAnswer2 = createQuizAnswer(quiz2, student1, true);
            createQuestionAnswer(quizAnswer2, quizQuestion3, true);
            createQuestionAnswer(quizAnswer2, quizQuestion4, true);

            QuizAnswer quizAnswer3 = createQuizAnswer(quiz3, student1, true);
            createQuestionAnswer(quizAnswer3, quizQuestion5, true);
            createQuestionAnswer(quizAnswer3, quizQuestion6, true);

            // 1 student answers 2 quizzes correctly.
            QuizAnswer quizAnswer4 = createQuizAnswer(quiz1, student2, true);
            createQuestionAnswer(quizAnswer4, quizQuestion1, true);
            createQuestionAnswer(quizAnswer4, quizQuestion2, true);

            QuizAnswer quizAnswer5 = createQuizAnswer(quiz2, student2, true);
            createQuestionAnswer(quizAnswer5, quizQuestion3, true);
            createQuestionAnswer(quizAnswer5, quizQuestion4, true);

            QuizAnswer quizAnswer6 = createQuizAnswer(quiz3, student2, true);
            createQuestionAnswer(quizAnswer6, quizQuestion5, true);
            createQuestionAnswer(quizAnswer6, quizQuestion6, false);

            // 1 student starts the quiz but does not submit it.
            QuizAnswer quizAnswer7 = createQuizAnswer(quiz1, student3, false);
            createQuestionAnswer(quizAnswer7, quizQuestion1, true);

            /*
             * The final statistics for this execution should be:
             * ESA:
             *  - numStudents = 3; numMore75CorrectQuestions = 2; numAtLeast3Quizzes = 2;
             * ESQ:
             *  - numQuizzes = 3; numUniqueAnsweredQuizzes = 2; averageQuizzesSolved = 3;
             * ESP:
             *  - numAvailable = 0; answeredQuestionsUnique = 6; averageQuestionsAnswered = 4.333;
             */
        }
        else if (courseExecution.getAcademicTerm().equals(academicTerms.get(2))) {
            // 4 students.
            Student student1 = createStudent("User 9 Name", courseExecution);
            Student student2 = createStudent("User 10 Name", courseExecution);
            Student student3 = createStudent("User 11 Name", courseExecution);
            Student student4 = createStudent("User 12 Name", courseExecution);

            // 3 quizzes.
            Quiz quiz1 = createQuiz("Quiz 5 Title", courseExecution);
            Quiz quiz2 = createQuiz("Quiz 6 Title", courseExecution);
            Quiz quiz3 = createQuiz("Quiz 7 Title", courseExecution);

            // 1 question for each quiz.
            QuizQuestion quizQuestion1 = createQuizQuestion(quiz1, 0, "Question 11 Title", "Question 11 Content", courseExecution, "Question 11 Option 1", "Question 11 Option 2", Question.Status.AVAILABLE);
            QuizQuestion quizQuestion2 = createQuizQuestion(quiz2, 0, "Question 12 Title", "Question 12 Content", courseExecution, "Question 12 Option 1", "Question 12 Option 2", Question.Status.AVAILABLE);
            QuizQuestion quizQuestion3 = createQuizQuestion(quiz3, 0, "Question 13 Title", "Question 13 Content", courseExecution, "Question 13 Option 1", "Question 13 Option 2", Question.Status.AVAILABLE);
            
            // 1 student answers 2 quizzes correctly.s
            QuizAnswer quizAnswer1 = createQuizAnswer(quiz1, student1, true);
            createQuestionAnswer(quizAnswer1, quizQuestion1, true);
            QuizAnswer quizAnswer2 = createQuizAnswer(quiz2, student1, true);
            createQuestionAnswer(quizAnswer2, quizQuestion2, true);

            // 1 student answers 1 quiz wrong.
            QuizAnswer quizAnswer4 = createQuizAnswer(quiz1, student2, true);
            createQuestionAnswer(quizAnswer4, quizQuestion1, false);

            // The other students dont answer any quizzes.

            /*
             * The final statistics for this execution should be:
             * ESA:
             *  - numStudents = 4; numMore75CorrectQuestions = 1; numAtLeast3Quizzes = 0;
             * ESQ:
             *  - numQuizzes = 3; numUniqueAnsweredQuizzes = 0.75; averageQuizzesSolved = 2;
             * ESP:
             *  - numAvailable = 3; answeredQuestionsUnique = 2; averageQuestionsAnswered = 0.75;
             */
        }
    }

    private Student createStudent(String name, CourseExecution courseExecution) {
        Student student = new Student(name, false);
        student.addCourse(courseExecution);
        userRepository.save(student);

        return student;
    }

    private Quiz createQuiz(String title, CourseExecution courseExecution) {
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setType(Quiz.QuizType.PROPOSED.toString());
        quiz.setCourseExecution(courseExecution);
        quiz.setCreationDate(DateHandler.now());
        quiz.setAvailableDate(DateHandler.now());
        quizRepository.save(quiz);

        return quiz;
    }

    private Question createQuestion(String title, String content, CourseExecution courseExecution, String content1, String content2, Question.Status status) {
        Question newQuestion = new Question();
        newQuestion.setTitle(title);
        newQuestion.setCourse(courseExecution.getCourse());
        MultipleChoiceQuestion questionDetails = new MultipleChoiceQuestion();
        newQuestion.setQuestionDetails(questionDetails);
        newQuestion.setStatus(status);
        questionRepository.save(newQuestion);

        Option option = new Option();
        option.setContent(content1);
        option.setCorrect(true);
        option.setSequence(0);
        option.setQuestionDetails(questionDetails);
        optionRepository.save(option);
        Option optionKO = new Option();
        optionKO.setContent(content2);
        optionKO.setCorrect(false);
        optionKO.setSequence(1);
        optionKO.setQuestionDetails(questionDetails);
        optionRepository.save(optionKO);

        return newQuestion;
    }

    private QuizQuestion createQuizQuestion(Quiz quiz, int sequence, String title, String content, CourseExecution courseExecution, String content1, String content2, Question.Status status) {
        Question question = createQuestion(title, content, courseExecution, content1, content2, status);
        QuizQuestion quizQuestion = new QuizQuestion(quiz, question, sequence);
        quizQuestionRepository.save(quizQuestion);

        return quizQuestion;
    }

    private void createQuestionAnswer(QuizAnswer quizAnswer, QuizQuestion quizQuestion, boolean correct) {
        QuestionAnswer questionAnswer = new QuestionAnswer();
        questionAnswer.setTimeTaken(1);
        questionAnswer.setQuizAnswer(quizAnswer);
        questionAnswer.setQuizQuestion(quizQuestion);

        MultipleChoiceQuestion questionDetails = (MultipleChoiceQuestion) quizQuestion.getQuestion().getQuestionDetails();
        Option option = null;

        List<Option> options = questionDetails.getOptions();

        if(correct) {
            option = options.get(0);
        }
        else {
            option = options.get(1);
        }

        MultipleChoiceAnswer answerDetails = new MultipleChoiceAnswer(questionAnswer, option);
        questionAnswer.setAnswerDetails(answerDetails);
        questionAnswerRepository.save(questionAnswer);
        answerDetailsRepository.save(answerDetails);
    }

    private QuizAnswer createQuizAnswer(Quiz quiz, Student student, boolean completed) {
        QuizAnswer quizAnswer = new QuizAnswer();
        quizAnswer.setCompleted(completed);
        quizAnswer.setCreationDate(DateHandler.now());
        quizAnswer.setAnswerDate(DateHandler.now());
        quizAnswer.setStudent(student);
        quizAnswer.setQuiz(quiz);
        quizAnswerRepository.save(quizAnswer);

        return quizAnswer;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void populateDemo() {
        Integer courseId = courseExecutionService.getDemoCourse().getCourseId();
        Integer courseExecutionId = courseExecutionService.getDemoCourse().getCourseExecutionId();

        Topic softwareArchitectureTopic = new Topic();
        softwareArchitectureTopic.setName("Software Architecture");
        softwareArchitectureTopic.setCourse(courseRepository.findById(courseId).orElseThrow(() -> new TutorException(ErrorMessage.COURSE_NOT_FOUND)));
        topicRepository.save(softwareArchitectureTopic);

        Topic softwareEngineeringTopic = new Topic();
        softwareEngineeringTopic.setName("Software Engineering");
        softwareEngineeringTopic.setCourse(courseRepository.findById(courseId).orElseThrow(() -> new TutorException(ErrorMessage.COURSE_NOT_FOUND)));
        topicRepository.save(softwareEngineeringTopic);

        List<Question> questions = questionRepository.findQuestions(courseId);
        questions.forEach(question -> {
            question.setStatus(Question.Status.AVAILABLE);
            question.addTopic(softwareEngineeringTopic);
        });

        Assessment assessment = new Assessment();
        assessment.setTitle("Software Engineering Questions");
        assessment.setStatus(Assessment.Status.AVAILABLE);
        assessment.setSequence(1);
        assessment.setCourseExecution(courseExecutionRepository.findById(courseExecutionId).orElseThrow(() -> new TutorException(ErrorMessage.COURSE_EXECUTION_NOT_FOUND)));
        TopicConjunction topicConjunction = new TopicConjunction();
        topicConjunction.addTopic(softwareEngineeringTopic);
        topicConjunction.setAssessment(assessment);
        assessmentRepository.save(assessment);

        Quiz inClassOneWayQuiz = new Quiz();
        inClassOneWayQuiz.setTitle("In Class Quiz One Way");
        inClassOneWayQuiz.setType(Quiz.QuizType.IN_CLASS.name());
        inClassOneWayQuiz.setCreationDate(DateHandler.now());
        inClassOneWayQuiz.setAvailableDate(DateHandler.now());
        inClassOneWayQuiz.setConclusionDate(DateHandler.now().plusHours(22));
        inClassOneWayQuiz.setCourseExecution(courseExecutionRepository.findById(courseExecutionId).orElseThrow(() -> new TutorException(ErrorMessage.COURSE_EXECUTION_NOT_FOUND)));
        inClassOneWayQuiz.setOneWay(true);
        inClassOneWayQuiz.setScramble(true);

        Quiz inClassQuiz = new Quiz();
        inClassQuiz.setTitle("In Class Quiz");
        inClassQuiz.setType(Quiz.QuizType.IN_CLASS.name());
        inClassQuiz.setCreationDate(DateHandler.now());
        inClassQuiz.setAvailableDate(DateHandler.now());
        inClassQuiz.setConclusionDate(DateHandler.now().plusHours(22));
        inClassQuiz.setCourseExecution(courseExecutionRepository.findById(courseExecutionId).orElseThrow(() -> new TutorException(ErrorMessage.COURSE_EXECUTION_NOT_FOUND)));
        inClassQuiz.setScramble(true);

        Quiz proposedQuiz = new Quiz();
        proposedQuiz.setTitle("Teacher Proposed");
        proposedQuiz.setType(Quiz.QuizType.PROPOSED.name());
        proposedQuiz.setCreationDate(DateHandler.now());
        proposedQuiz.setAvailableDate(DateHandler.now());
        proposedQuiz.setCourseExecution(courseExecutionRepository.findById(courseExecutionId).orElseThrow(() -> new TutorException(ErrorMessage.COURSE_EXECUTION_NOT_FOUND)));
        proposedQuiz.setScramble(true);

        Quiz scrambledQuiz = new Quiz();
        scrambledQuiz.setTitle("Non Scrambled");
        scrambledQuiz.setType(Quiz.QuizType.PROPOSED.name());
        scrambledQuiz.setCreationDate(DateHandler.now());
        scrambledQuiz.setAvailableDate(DateHandler.now());
        scrambledQuiz.setCourseExecution(courseExecutionRepository.findById(courseExecutionId).orElseThrow(() -> new TutorException(ErrorMessage.COURSE_EXECUTION_NOT_FOUND)));

        questions.forEach(question -> {
            if (questionsInQuizzes.contains(question.getId())) {
                new QuizQuestion(inClassOneWayQuiz, question, inClassOneWayQuiz.getQuizQuestionsNumber());
                new QuizQuestion(inClassQuiz, question, inClassQuiz.getQuizQuestionsNumber());
                new QuizQuestion(proposedQuiz, question, proposedQuiz.getQuizQuestionsNumber());
                new QuizQuestion(scrambledQuiz, question, scrambledQuiz.getQuizQuestionsNumber());
            }
        });

        quizRepository.save(inClassOneWayQuiz);
        quizRepository.save(inClassQuiz);
        quizRepository.save(proposedQuiz);
        quizRepository.save(scrambledQuiz);

        // Creates the executions and stats for the tests.
        createExecutions();
    }
}
