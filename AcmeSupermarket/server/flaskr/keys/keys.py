from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives.asymmetric import rsa
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.exceptions import InvalidSignature


import click
from flask import current_app
from flask.cli import with_appcontext
from ..utils import string_to_bytes


def public_key_to_bytes(key):
    """Convert the given public key into bytes"""
    return key.public_bytes(
        encoding=serialization.Encoding.DER,
        format=serialization.PublicFormat.SubjectPublicKeyInfo
    )


def user_key_from_bytes(key_as_bytes):
    """Read the user public key from the given bytes"""
    return serialization.load_der_public_key(
        key_as_bytes,
        backend=default_backend()
    )


def sign(private_key, message):
    """Sign the given message using the given private key"""
    return private_key.sign(
        string_to_bytes(message),
        padding=padding.PSS(
            mgf=padding.MGF1(hashes.SHA1()),
            salt_length=20
        ),
        algorithm=hashes.SHA256()
    )


def verify(public_key, signature, data):
    """Verify the given signature using the given public key.
    Returns true if verified, False otherwise"""
    is_signature_correct = True

    try:
        public_key.verify(
            signature=signature,
            data=data,
            padding=padding.PKCS1v15(),
            # PSS(
            #     mgf=padding.MGF1(hashes.SHA256()),
            #     salt_length=padding.PSS.MAX_LENGTH
            # ),
            algorithm=hashes.SHA256()
        )
    except InvalidSignature:
        is_signature_correct = False

    return is_signature_correct


def init_keys(keys_folder: str):
    """Generate and store both public and private keys in
    the given folder in file 'keys.cfg'"""
    private_key = rsa.generate_private_key(
        public_exponent=65537,
        key_size=512,  # 512 bits
        backend=default_backend()
    )
    public_key = private_key.public_key()

    # Writing private key
    with open('%s/private_key.pem' % keys_folder, 'wb') as f:
        f.write(
            private_key.private_bytes(
                encoding=serialization.Encoding.PEM,
                format=serialization.PrivateFormat.PKCS8,
                encryption_algorithm=serialization.NoEncryption()
            )
        )

    # Writing public key
    with open('%s/public_key.pem' % keys_folder, 'wb') as f:
        f.write(
            public_key.public_bytes(
                encoding=serialization.Encoding.PEM,
                format=serialization.PublicFormat.SubjectPublicKeyInfo
            )
        )


def load_keys(keys_folder: str):
    """Load the keys from the .pem files in the given keys_folder"""
    # Reading private key
    with open('%s/private_key.pem' % keys_folder, "rb") as f:
        private_key = serialization.load_pem_private_key(
            f.read(),
            password=None,
            backend=default_backend()
        )

    # Reading public key
    with open('%s/public_key.pem' % keys_folder, "rb") as f:
        #print(f.read())
        public_key = serialization.load_pem_public_key(
            f.read(),
            backend=default_backend()
        )

    return (private_key, public_key)


@click.command('gen-keys')
@with_appcontext
def gen_new_keys():
    """Clear the existing keys and generate new ones."""
    init_keys(current_app.instance_path)
    click.echo('Generated new server\'s keys.')


def init_app(app):
    app.cli.add_command(gen_new_keys)
