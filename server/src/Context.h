// $Id$
// Description: Singleton class for pulling information about the active window
// (C) Cyberpad Technologies 2011
#ifndef CONTEXT_H_
#define CONTEXT_H_

#include <QString>

#include "OS.h"

class Context
{
    // static
private:
    static Context* sInstance;

public:
    static Context& Get();

    // non-static
private:
    Context();
    ~Context();
    void Init();

public:
    QString Title();
    QString Executable();
    int ProcessID();
};

#endif // CONTEXT_H_
