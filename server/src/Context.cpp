#include "Context.h"

#include <cstdio>

Context* Context::sInstance = NULL;

Context& Context::Get()
{
    if (sInstance == NULL)
    {
        sInstance = new Context();
    }
    return *sInstance;
}

Context::Context()
{
    Init();
}

Context::~Context()
{

}

void Context::Init()
{
    #ifdef OS_WINDOWS

    #endif
    #ifdef OS_LINUX

    #endif
}

QString Context::Title()
{
    #ifdef OS_WINDOWS
        HWND window = GetForegroundWindow();
        if (window == NULL)
        {
            return QString("");
        }
        int titleLength = GetWindowTextLength(window) * 2; // For some reason, SWTL seems to undershoot by some amount, just double it for now
        LPTSTR ctitle = new TCHAR[titleLength];
        int copied = GetWindowText(window, ctitle, titleLength);

        //DEBUG
        printf("Title: %s\n", ctitle);

        QString qstitle(ctitle);
        return qstitle;
    #endif
}

QString Context::Executable() // Not yet implemented
{
    #ifdef OS_WINDOWS
        HWND window = GetForegroundWindow();
        if (window == NULL)
        {
            return QString("");
        }
        return QString("");
    #endif
}

int Context::ProcessID()
{
    #ifdef OS_WINDOWS
        HWND window = GetForegroundWindow();
        if (window == NULL)
        {
            return 0;
        }
        DWORD pid = GetWindowThreadProcessId(window, NULL);

        printf("pid: %i", int(pid));

        return int(pid);
    #endif
}
