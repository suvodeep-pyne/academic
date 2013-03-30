#include <stdlib.h>
#include <string.h>
#include <stdio.h>
int T;
typedef struct {
    int x,y,z;
}point;
typedef struct {
    int x1,x2,y;
}segment;
typedef struct {
    int P;
    int z;
    point p[201];
    segment s[201];
}face;
typedef struct {
    int F;
    face f[251];
    int U;
}bulk;
bulk b;
int maxx,maxy,minx,miny;
void get_test_num(int *T);
void init_bulk(bulk *b);
void get_a_bulk(bulk *b);
void get_face_num(int *F);
void get_a_face(face *f);
void get_point_num(int *P);
void get_a_point(point *p);
void update_max_min(point *p);
void format_bulk(bulk *b);
void mark_nonXOY(bulk *b);
void sort_face_by_Z(bulk *b);
int cmp_face_by_Z(const void *a , const void *b);
void point2segment(face *f);
void sort_segment_by_Y(face *f);
int cmp_segment_by_Y(const void *a, const void *b);
void count_unit(bulk *b);
int in_the_face(face *f,double x,double y);
int is_odd_segment(face *f, segment *s);
int is_odd_face(face *f);
int get_face_area(face *f);

int main()
{
    int i,j;
    get_test_num(&T);
    while((T--)>0)
    {
        init_bulk(&b);
        get_a_bulk(&b);
        format_bulk(&b);
        count_unit(&b);
        printf("The bulk is composed of %d units.\n",b.U);
    }
    return 0;
}

void get_test_num(int *T)
{
    scanf("%i",T);
}

void init_bulk(bulk *b)
{
    memset(b,0,sizeof(bulk));
    maxx=0;maxy=0;minx=1001;miny=1001;
}

void get_a_bulk(bulk *b)
{
    int i;
    get_face_num(&b->F);
    for(i=0;i<b->F;i++)
        get_a_face(&b->f[i]);
}

    void get_face_num(int *F)
    {
        scanf("%i",F);
    }

    void get_a_face(face *f)
    {
        int i;
        get_point_num(&f->P);
        for(i=0;i<f->P;i++)
        {
            get_a_point(&f->p[i]);
            update_max_min(&f->p[i]);
        }
    }

        void get_point_num(int *P)
        {scanf("%i",P);}

        void get_a_point(point *p)
        {scanf("%i%i%i",&p->x,&p->y,&p->z);}

        void update_max_min(point *p)
        {
            maxx=(maxx>p->x?maxx:p->x);
            maxy=(maxy>p->y?maxy:p->y);
            minx=(minx<p->x?minx:p->x);
            miny=(miny<p->y?miny:p->y);
        }

void format_bulk(bulk *b)
{
    int i;
    mark_nonXOY(b);
    sort_face_by_Z(b);
    for(i=0;b->f[i].z < 1001;i++)
    {
        point2segment(&b->f[i]);
        sort_segment_by_Y(&b->f[i]);
    }
}

    void mark_nonXOY(bulk *b)
    {
        int i,j;
        for(i=0;i<b->F;i++)
        {
            b->f[i].z=b->f[i].p[0].z;
            for(j=1;j<b->f[i].P;j++)
            {
                if(b->f[i].p[j].z!=b->f[i].z)break;
            }
            if(j!=b->f[i].P)b->f[i].z=1001;
        }
    }

    void sort_face_by_Z(bulk *b)
    {
        qsort(b->f,b->F,sizeof(face),cmp_face_by_Z);
    }

        int cmp_face_by_Z(const void *a , const void *b)
        {
            return (*(face*)a).z-(*(face*)b).z;
        }

    void point2segment(face *f)
    {
        int i,temp;
        if(f->p[0].y==f->p[f->P-1].y)
        {
            f->s[0].x1=f->p[f->P-1].x;
            f->s[0].x2=f->p[0].x;
            if(f->s[0].x1>f->s[0].x2)
                temp=f->s[0].x1,f->s[0].x1=f->s[0].x2,f->s[0].x2=temp;
            f->s[0].y=f->p[0].y;
        }
        else f->s[0].y=1001;
        for(i=1;i<f->P;i++)
        {
            if(f->p[i].y==f->p[i-1].y)
            {
                f->s[i].x1=f->p[i-1].x;
                f->s[i].x2=f->p[i].x;
                if(f->s[i].x1>f->s[i].x2)
                    temp=f->s[i].x1,f->s[i].x1=f->s[i].x2,f->s[i].x2=temp;
                f->s[i].y=f->p[i].y;
            }
            else f->s[i].y=1001;
        }
    }
    void sort_segment_by_Y(face *f)
    {
        qsort(f->s,f->P,sizeof(segment),cmp_segment_by_Y);
    }

        int cmp_segment_by_Y(const void *a, const void *b)
        {
            return (*(segment*)a).y - (*(segment*)b).y;
        }

void count_unit(bulk *b)
{
    int i,area=0;
    for(i=0;b->f[i].z<1001;i++)
    {
        area=get_face_area(&b->f[i]);
        b->U+=(is_odd_face(&b->f[i]))*(b->f[i].z)*area;
    }
}
    int is_odd_face(face *f)
    {
        face *f0;
        int s;
        double x=(double)f->s[0].x1 + 0.5;
        double y=(double)f->s[0].y + 0.5;
        for(f0=&b.f[0],s=0; f0->z < f->z; f0++)
            if(in_the_face(f0,x,y))s++;
        if(s%2==0)return 1;
        else return -1;
    }
        int in_the_face(face *f,double x,double y)
        {
            int i,s=0;
            for(i=0;f->s[i].y<y;i++)
                if(f->s[i].x1<x&&x<f->s[i].x2)s++;
            if(s%2==1)return 1;
            else return 0;
        }

    int get_face_area(face *f)
    {
        int i,area=0;
        for(i=0;f->s[i].y<1001;i++)
            area+=(is_odd_segment(f,&f->s[i]))*(f->s[i].y)*(f->s[i].x2-f->s[i].x1);
        return area;
    }
        int is_odd_segment(face *f, segment *s)
        {
            int w=0;
            segment *s0=&f->s[0];
            double x=(double)s->x1+0.5;
            for(;s0->y<s->y;s0++)
                if(s0->x1 < x && x < s0->x2)
                    w++;
            if(w%2==0)return 1;
            else return -1;
        }