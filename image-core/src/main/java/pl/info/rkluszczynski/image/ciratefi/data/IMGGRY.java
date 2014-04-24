package pl.info.rkluszczynski.image.ciratefi.data;

/**
 * Created by Rafal on 2014-04-24.
 */
public class IMGGRY {
//<<<<<<<<<<<<<<<<<<<<< class IMGGRY <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// Eu acrescentei os modos de acesso at?
// Acrescentei maxxC etc.
// Tenho que fazer a mesma coisa nas outras classes de imagens

    private int[] vet;
    private int[][] lin;
    private int backgv;
    private int nlv, ncv, lcv, ccv, nv;


    public int nl() {
        return nlv;
    }

    public int nc() {
        return ncv;
    }

    public int nx() {
        return ncv;
    }

    public int ny() {
        return nlv;
    }

    public int n() {
        return nv;
    }

    public int maxl() {
        return nlv - lcv - 1;
    }

    public int minl() {
        return -lcv;
    }

    public int maxc() {
        return ncv - ccv - 1;
    }

    public int minc() {
        return -ccv;
    }

    public int maxx() {
        return ncv - ccv - 1;
    }

    public int minx() {
        return -ccv;
    }

    public int maxy() {
        return lcv;
    }

    public int miny() {
        return -(nlv - lcv - 1);
    }


    public int get(int l, int c) {
        if (0 <= l && l < nlv && 0 <= c && c < ncv) return lin[l][c];
        else return backgv;
    }

    //    public int get(int l, int c, char modo);
    public int get(int i) {
        if (0 <= i && i < nv) return vet[i];
        else return backgv;
    }


    /*

class IMGGRY {
private:
  BYTE *vet; BYTE **lin;
  BYTE backgv; int nlv,ncv,lcv,ccv,nv;

  void aloca(int nlp, int ncp);
  void desaloca();
  void copia(const IMGGRY& x);
public:
  void fill(BYTE e1);
  void fill(BYTE* v);
  void resize(int nlp, int ncp);
  //void resize(int nlp, int ncp, BYTE e1);
  //void resize(int nlp, int ncp, BYTE* v);
  //typedef BYTE VALUE_TYPE;

  friend void ImpJpg(IMGGRY& a, string nome, int quality);
  friend void LeJpg(IMGGRY& a, string nome);
  friend class I3DGRY;
  friend class I4DGRY;
  friend class VIDIN;
  friend class VIDOUT;
  friend class IMGYUV;


  int& lc() { return lcv; }
  int& cc() { return ccv; }
  BYTE& backg() { return backgv; }

  IMGGRY(int nlp=0, int ncp=0);        // (default) constructor
  IMGGRY(const IMGGRY& x);             // copy constructor
  IMGGRY& operator= (const IMGGRY& x); // copy assignment
  ~IMGGRY();                           // destructor

  IMGGRY(int nlp, int ncp, BYTE e1);   // constructor 3 args
  IMGGRY(int nlp, int ncp, BYTE e1, BYTE e2 ...); // constructor n args
  //IMGGRY(int nlp, int ncp, BYTE* v);


  BYTE& atf(int i) { // Free - Sem verificacao de indice invalido
    return vet[i];
  }
  BYTE& ate(int i) { // Modo geracao de erro se indexar fora do dominio
    if (0<=i && i<nv) return vet[i];
    else erro("Indice invalido");
  }

  BYTE& atf(int l, int c) { // Free - Sem verificacao de indice invalido
    return lin[l][c];
  }
  BYTE& atn(int l, int c) { // Modo normal com backg
    if (0<=l && l<nlv && 0<=c && c<ncv) return lin[l][c];
    else return backgv;
  }
  BYTE& atx(int l, int c) { // Modo extendido
    if (0<=l && l<nlv && 0<=c && c<ncv) return lin[l][c];
    if (l<0) {
      if (c<0) return lin[0][0];
      else if (c>=ncv) return lin[0][ncv-1];
      else return lin[0][c];
    } else if (l>=nlv) {
      if (c<0) return lin[nlv-1][0];
      else if (c>=ncv) return lin[nlv-1][ncv-1];
      else return lin[nlv-1][c];
    } else {
      if (c<0) return lin[l][0];
      else if (c>=ncv) return lin[l][ncv-1];
      else erro("Erro inesperado");
    }
    return backg();
  }
  BYTE& ate(int l, int c) { // Modo geracao de erro se indexar fora do dominio
    if (0<=l && l<nlv && 0<=c && c<ncv) return lin[l][c];
    else erro("Indice invalido");
  }
  BYTE& atr(int l, int c) { // Modo replicado
    return lin[modulo(l,nlv)][modulo(c,ncv)];
  }
  BYTE& atc(int l, int c) { // modo LC centralizado
    l=l+lcv; c=c+ccv;
    if (0<=l && l<nlv && 0<=c && c<ncv) return lin[l][c];
    else return backgv;
  }
  BYTE& ats(int l, int c) { // repetido e centralizado
    l=l+lcv; c=c+ccv;
    return lin[modulo(l,nlv)][modulo(c,ncv)];
  }
  BYTE& atN(int l, int c) { // modo normal XY (com backg)
    if (0<=l && l<ncv && 0<=c && c<nlv) return lin[nlv-1-c][l];
    else return backgv;
  }
  BYTE& atX(int l, int c) { // modo XY centralizado e extendido
    int x=l; int y=c;
    l=lcv-y; c=ccv+x;
    if (0<=l && l<nlv && 0<=c && c<ncv) return lin[l][c];
    if (l<0) {
      if (c<0) return lin[0][0];
      else if (c>=ncv) return lin[0][ncv-1];
      else return lin[0][c];
    } else if (l>=nlv) {
      if (c<0) return lin[nlv-1][0];
      else if (c>=ncv) return lin[nlv-1][ncv-1];
      else return lin[nlv-1][c];
    } else {
      if (c<0) return lin[l][0];
      else if (c>=ncv) return lin[l][ncv-1];
      else erro("Erro inesperado");
    }
    return backg();
  }
  BYTE& atC(int l, int c) { // modo XY centralizado
    int x=l; int y=c;
    l=lcv-y; c=ccv+x;
    if (0<=l && l<nlv && 0<=c && c<ncv) return lin[l][c];
    else return backgv;
  }

  private:
  void copia(IMGBIN& x);
  public:
  IMGGRY(IMGBIN x);
  IMGGRY& operator= (IMGBIN x);

  private:
  void copia(IMGYUV& x);
  public:
  IMGGRY(IMGYUV x);
  IMGGRY& operator= (IMGYUV x);

  private:
  void copia(IMGCOR& x);
  public:
  IMGGRY(IMGCOR x);
  IMGGRY& operator= (IMGCOR x);

  private:
  void copia(IMGFLT& x);
  public:
  IMGGRY(IMGFLT x);
  IMGGRY& operator= (IMGFLT x);

  private:
  void copia(IMGDBL& x);
  public:
  IMGGRY(IMGDBL x);
  IMGGRY& operator= (IMGDBL x);

  private:
  void copia(IMGSHT& x);
  public:
  IMGGRY(IMGSHT x);
  IMGGRY& operator= (IMGSHT x);

  void LeTxt(string nome);
  void ImpTxt(string nome);
  void LeTga(string nome);
  void ImpTga(string nome);
  void LeTgaP(string nome); // Le TGA com palette (aprox para cinza)
  void LeTgaI(string nome); // Le indice de TGA com palette
};

     */


}
