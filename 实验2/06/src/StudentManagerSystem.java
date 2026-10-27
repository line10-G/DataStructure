public class StudentManagerSystem {
    static Student[] students = new Student[10];
    static int studentNumber =0;

    public enum Sex{
        F,M
    }

    public enum Subject{
        English, Math
    }

    public final static class Student{
        int number;
        String name;
        Sex sex;
        int englishScore;
        int mathScore;
        public Student(int number,String name,Sex sex,int englishScore,int mathScore){
            this.number=number;
            this.name=name;
            this.sex = sex;
            this.englishScore = englishScore;
            this.mathScore = mathScore;
        }
    }

    public static void insert(Student student){
        if(students.length== studentNumber){
            resizeL();
        }
        students[studentNumber++] = student;
    }

    private static void resizeL(){
        Student[] newStudents = new Student[students.length*2];
        System.arraycopy(students, 0, newStudents, 0, studentNumber);
        students = newStudents;
    }

    public static Student find(int number){
        for(int i = 0; i< studentNumber; i++){
            if(students[i].number==number){
                return students[i];
            }
        }
        return null;
    }

    public static void display(int number){
        Student s = find(number);
        if(s==null){
            System.out.println("Not found");
            return;
        }
        System.out.println(s.number+" "+s.name+" "+s.sex+" "+ s.englishScore+" "+s.mathScore);
    }

    public static void change(int number,int score,Subject subject){
        Student s = find(number);
        if(s==null){
            System.out.println("Not found");
            return;
        }
        if(subject == Subject.Math){
            s.mathScore = score;
        }else if(subject == Subject.English){
            s.englishScore = score;
        }
    }

    public static void delete(int number){
        for(int i = 0; i < studentNumber; i++){
            if(students[i].number == number){
                for(int j = i; j < studentNumber - 1; j++){
                    students[j] = students[j+1];
                }
                students[studentNumber-1] = null;
                studentNumber--;
                return;
            }
        }
        System.out.println("Not found");
    }

    public static void main(String[] args) {
        Student s1 = new Student(2008001,"Alan",Sex.F,93,88);
        Student s2 = new Student(2008002,"Danie",Sex.M,75,69);
        Student s3 = new Student(2008003,"Helen",Sex.M,56,77);
        Student s4 = new Student(2008004,"Bill",Sex.F,87,90);
        Student s5 = new Student(2008005,"Peter",Sex.M,79,86);
        Student s6 = new Student(2008006,"Amy",Sex.F,68,75);
        insert(s1);
        insert(s2);
        insert(s3);
        insert(s4);
        insert(s5);
        insert(s6);

        display(2008001);
        display(2008007);
        System.out.println();

        display(2008006);
        change(2008006,100,Subject.English);
        display(2008006);
        change(2008006,100,Subject.Math);
        display(2008006);
        System.out.println();

        delete(2008003);
        for(int i = 0; i< studentNumber; i++){
            display(students[i].number);
        }
    }
}
