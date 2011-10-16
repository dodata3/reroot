// $Id$
// Description: Declares a cipher interface for objects to use
// (C) Cyberpad Technologies 2011
#ifndef CIPHER_H_
#define CIPHER_H_

#include <QByteArray>
#include "Global.h"

class Cipher
{
protected:
    virtual void Encrypt( SecretKey key, QByteArray& ioData );
    virtual bool Decrypt( SecretKey key, QByteArray& ioData );
};

#endif // CIPHER_H_
