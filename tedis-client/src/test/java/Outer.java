class Outer
{
    private Integer a;
    private Long b;

    class Inner
    {
        public void foo()
        {
            System.out.println("a and b are " + a + " " + b);
        }
    }
}