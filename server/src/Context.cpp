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
        DWORD copied = GetWindowText(window, ctitle, titleLength);

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
            WindowsError("GetForegroundWindow");
            return QString("");
        }
        DWORD pid;
        LPDWORD ppid = &pid;
        GetWindowThreadProcessId(window, ppid);
        WindowsError("GetWindowThreadProcessId");

        const size_t nameLength = 256; // There doesn't seem to be any way of fetching the file name length, but beyond 256 seems excessive
        LPTSTR cname = new TCHAR[nameLength];

        //HANDLE handle = OpenProcess(PROCESS_QUERY_LIMITED_INFORMATION, FALSE, pid);
        HANDLE handle = OpenProcess(PROCESS_QUERY_INFORMATION, FALSE, pid);
        WindowsError("OpenProcess");
        DWORD copied = GetProcessImageFileName(handle, cname, nameLength);
        if (CloseHandle(handle) == 0)
        {
            WindowsError("GetProcessImageFileName");
        }

        printf("name: %s\n", cname);

        return QString(cname);
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
        DWORD pid;
        LPDWORD ppid = &pid;
        GetWindowThreadProcessId(window, ppid);

        printf("pid: %i\n", int(pid));

        return int(pid);
    #endif
}
