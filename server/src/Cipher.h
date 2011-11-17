// $Id$
// Description: Declares a cipher interface for objects to use
// (C) Cyberpad Technologies 2011
#ifndef CIPHER_H_
#define CIPHER_H_

#include <cryptopp/rsa.h>
#include <QByteArray>
#include "Global.h"

class Cipher
{
public:
    Cipher();
    static void GenerateKeypair( CryptoPP::RSA::PublicKey& oPubKey, CryptoPP::RSA::PrivateKey& oPrivKey );
protected:
    virtual QString Encrypt( CryptoPP::RSA::PublicKey inEncKey, CryptoPP::RSA::PrivateKey inSignKey, QString& ioMessage );
    virtual bool Decrypt( CryptoPP::RSA::PrivateKey inEncKey, CryptoPP::RSA::PublicKey inSignKey, QString& ioMessage, QString& inSignature );
};

#endif // CIPHER_H_
