sumar(x : Int, y : Int) : Int {
  x + y;
};

restar(a : Int, b : Int) : Int {
  a - b;
};

multiplicar(m : Int, n : Int) : Int {
  m * n;
};

dividir(dividendo : Int, divisor : Int) : Int {
  dividendo / divisor;
};

comparar(c1 : Int, c2 : Int) : Bool {
  c1 < c2;
};

compararIgual(c1 : Int, c2 : Int) : Bool {
  c1 = c2;
};

negacionEntera(x : Int) : Int {
  ~x;
};

negacionBooleana(flag : Bool) : Bool {
  not flag;
};

asignacionEjemplo() : Int {
  variable <- 42;
};

expresionCompuesta() : Int {
  {
    a <- 1;
    b <- 2;
    a + b * (a + b);
  };
};

boolConstantes() : Bool {
  true;
};

otraBoolConstante() : Bool {
  false;
};
